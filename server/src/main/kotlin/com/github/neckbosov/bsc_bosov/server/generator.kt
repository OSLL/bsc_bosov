package com.github.neckbosov.bsc_bosov.server

import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import com.github.neckbosov.bsc_bosov.server.dao.VariantsOps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import java.util.concurrent.TimeUnit

private val programRunMutex = Mutex()
private val generationDir = (System.getenv("GENERATION_DIR")?.plus("/")) ?: "./generation_data/"

@kotlinx.serialization.Serializable
data class GenerationConfig(
    val extension: String,
    val formatCmd: String?,
    val compileCmd: String?,
    val runCmd: String
)

object GenerationConfigs {
    private val configFile = javaClass.classLoader.getResourceAsStream("runners.json")
    private val configs: Map<String, GenerationConfig> = Json.decodeFromString(configFile!!.bufferedReader().readText())

    fun getConfig(key: String): GenerationConfig? = configs[key]
}

private fun parseCmd(cmd: String, filename: String): Array<String> {
    return cmd.replace("\$PROGRAM_PATH", filename).replace("\$EXE_PATH", "$filename.out")
        .split(" ").toTypedArray()
}

suspend fun generateProgramData(
    tag: ProgramLanguageTag,
    params: ProgramAttributes,
    code: String,
    variantsDB: VariantsOps
): ProgramData {
    val config = GenerationConfigs.getConfig(tag.tagName)!!
    val filename: String = "program." + config.extension
    val codeFilePath = generationDir + filename
    val formatCommand: Array<String>? = config.formatCmd?.let { parseCmd(it, codeFilePath) }
    val compileCommand: Array<String>? = config.compileCmd?.let { parseCmd(it, codeFilePath) }
    val runCommand: Array<String> = parseCmd(config.runCmd, codeFilePath) + arrayOf(">", generationDir + "output.txt")
    val imageFile = generationDir + "program.png"
    val imageCommand = arrayOf("pygmentize", "-f", "img", "-o", imageFile)
    val result = programRunMutex.withLock {
        val formattedCode = if (formatCommand != null) {
            withContext(Dispatchers.IO) {
                File(codeFilePath).bufferedWriter().use {
                    it.write(code)
                }
                val success = ProcessBuilder(*formatCommand, codeFilePath)
                    .start()
                    .waitFor(10, TimeUnit.SECONDS)
                File(codeFilePath).bufferedReader().use {
                    it.readText()
                }
            }
        } else {
            code
        }
        val image = withContext(Dispatchers.IO) {
            val success = ProcessBuilder(*imageCommand, codeFilePath)
                .start()
                .waitFor(10, TimeUnit.SECONDS)
            File(imageFile).readBytes()
        }

        if (compileCommand != null) {
            withContext(Dispatchers.IO) {
                val success = ProcessBuilder(*compileCommand)
                    .start()
                    .waitFor(10, TimeUnit.SECONDS)
            }
        }
        val answer = withContext(Dispatchers.IO) {
            val proc = ProcessBuilder(*runCommand)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()
            val success = proc.waitFor(30, TimeUnit.SECONDS)
            proc.inputStream.bufferedReader().readText()
        }
        ProgramData(
            params,
            image,
            formattedCode,
            answer
        )
    }
    variantsDB.addVariant(result.parameters, result.image, result.codeText, result.answer)
    return result
}
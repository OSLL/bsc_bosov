package com.github.neckbosov.bsc_bosov.server

import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import com.github.neckbosov.bsc_bosov.dsl.tags.PythonTag
import com.github.neckbosov.bsc_bosov.server.dao.VariantsOps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.TimeUnit

private val programRunMutex = Mutex()
private val generationDir = (System.getenv("GENERATION_DIR")?.plus("/")) ?: "./generation_data/"

suspend fun generateProgramData(
    tag: ProgramLanguageTag,
    params: ProgramAttributes,
    code: String,
    variantsDB: VariantsOps
): ProgramData {
    val filename: String
    val formatCommand: Array<String>
    val compileCommand: Array<String>?
    val runCommand: Array<String>
    when (tag) {
        is PythonTag -> {
            filename = "program.py"
            formatCommand = arrayOf("autopep8", "-i")
            compileCommand = null
            runCommand = arrayOf("python3", generationDir + "program.py", ">", generationDir + "output.txt")
        }
        else -> error("This program language is not supported yet")
    }
    val codeFilePath = generationDir + filename
    val imageFile = generationDir + "program.png"
    val imageCommand = arrayOf("pygmentize", "-f", "img", "-o", imageFile)
    val result = programRunMutex.withLock {
        val formattedCode = withContext(Dispatchers.IO) {
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
        val image = withContext(Dispatchers.IO) {
            val success = ProcessBuilder(*imageCommand, codeFilePath)
                .start()
                .waitFor(10, TimeUnit.SECONDS)
            File(imageFile).readBytes()
        }
        @Suppress("SENSELESS_COMPARISON")
        if (compileCommand != null) {
            withContext(Dispatchers.IO) {
                val success = ProcessBuilder(*compileCommand, codeFilePath)
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
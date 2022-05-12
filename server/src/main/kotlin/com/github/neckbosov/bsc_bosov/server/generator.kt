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
private val programRunDirectory = System.getProperty("user.dir")

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
            runCommand = arrayOf("python3", "program.py", ">", "output.txt")
        }
        else -> error("This program language is not supported yet")
    }
    val imageFile = "program.png"
    val imageCommand = arrayOf("pygmentize", "-f", "img", "-o", imageFile)
    val result = programRunMutex.withLock {
        val formattedCode = withContext(Dispatchers.IO) {
            File(filename).bufferedWriter().use {
                it.write(code)
            }
            val success = ProcessBuilder(*formatCommand, filename)
                .directory(File(programRunDirectory))
                .start()
                .waitFor(10, TimeUnit.SECONDS)
            File(filename).bufferedReader().use {
                it.readText()
            }
        }
        val image = withContext(Dispatchers.IO) {
            val success = ProcessBuilder(*imageCommand, filename)
                .directory(File(programRunDirectory))
                .start()
                .waitFor(10, TimeUnit.SECONDS)
            File(imageFile).readBytes()
        }
        val answer = withContext(Dispatchers.IO) {
            val proc = ProcessBuilder(*runCommand)
                .directory(File(programRunDirectory))
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
package com.github.neckbosov.bsc_bosov.tasks

import com.github.neckbosov.bsc_bosov.dsl.tags.PythonTag
import com.github.neckbosov.bsc_bosov.dsl.template.ProgramTemplate
import com.github.neckbosov.bsc_bosov.dsl.template.dslModule
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun main() {
    val task1Template = task1()
    val json = Json {
        serializersModule = dslModule
    }
    val s = json.encodeToString(task1Template)
    println(s)
    val t = json.decodeFromString<ProgramTemplate<PythonTag>>(s)

}
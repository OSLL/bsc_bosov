package com.github.neckbosov.bsc_bosov.tasks

import com.github.neckbosov.bsc_bosov.common.Task
import com.github.neckbosov.bsc_bosov.dsl.tags.PythonTag
import com.github.neckbosov.bsc_bosov.dsl.template.dslModule
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun main() {
    val taskTemplate = task()
    val taskName = "StringLen"
    val tag = PythonTag
    val task = Task(
        taskName,
        tag,
        taskTemplate
    )
    val dslJson = Json { serializersModule = dslModule }
    println(dslJson.encodeToString(task))
    val host = "0.0.0.0"
    val port = 8080
    val client = HttpClient {
        install(ContentNegotiation) {
            json(dslJson)
        }
    }
    runBlocking {
        client.use {
            it.post("http://$host:$port/add_task") {
                contentType(ContentType.Application.Json)
                setBody(task)
            }
        }
    }
}
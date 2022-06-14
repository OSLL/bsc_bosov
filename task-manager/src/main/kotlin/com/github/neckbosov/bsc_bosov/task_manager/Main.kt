package com.github.neckbosov.bsc_bosov.task_manager

import com.github.neckbosov.bsc_bosov.common.DeletedTask
import com.github.neckbosov.bsc_bosov.common.Task
import com.github.neckbosov.bsc_bosov.dsl.tags.CppTag
import com.github.neckbosov.bsc_bosov.dsl.template.dslModule
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    val parser = ArgParser("task-manager")
    val command by parser.argument(ArgType.Choice(listOf("add", "delete"), { it }), fullName = "Command")
    val host by parser.option(ArgType.String, fullName = "host", description = "Host of code generator")
        .default("0.0.0.0")
    val port by parser.option(ArgType.Int, fullName = "port", description = "Port of code generator").default(8080)
    parser.parse(args)
    val tasks = listOf(Task("StringLenCpp", CppTag, task()))
    val deletedTasks = listOf<String>("StringLenCpp")
    val dslJson = Json {
        serializersModule = dslModule
        prettyPrint = true
    }
    println(dslJson.encodeToString(tasks))
    val client = HttpClient {
        install(ContentNegotiation) {
            json(dslJson)
        }
    }
    runBlocking {
        client.use {
            when (command) {
                "add" -> {

                    for (task in tasks) {
                        it.request {
                            contentType(ContentType.Application.Json)
                            method = HttpMethod.Post
                            url("http://$host:$port/add_task")
                            setBody(task)
                        }
                    }
                }
                "delete" -> {
                    for (task in deletedTasks) {
                        println("task")
                        it.request {
                            contentType(ContentType.Application.Json)
                            method = HttpMethod.Delete
                            val deletedTask = DeletedTask(task)
                            url("http://$host:$port/delete_task")
                            setBody(deletedTask)
                        }
                    }
                }
                else -> error("unknown command")
            }

        }
    }
}
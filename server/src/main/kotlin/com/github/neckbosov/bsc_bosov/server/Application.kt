package com.github.neckbosov.bsc_bosov.server

import com.github.neckbosov.bsc_bosov.code_mapper.PythonMapper
import com.github.neckbosov.bsc_bosov.tasks.task1
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.jetty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*

fun main() {
    embeddedServer(Jetty, port = 8080) {
        install(ContentNegotiation) {
            json()
        }
        routing {
            get("/get_source") {
                val task = call.request.queryParameters["task"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val seed =
                    call.request.queryParameters["seed"]?.toLong() ?: return@get call.respond(HttpStatusCode.BadRequest)
                val attributes = call.request.queryParameters.toMap().minus("seed")
                val program = task1().fill(seed, attributes)
                val code = PythonMapper.generateCode(program)
                call.respond(HttpStatusCode.OK, code)
            }
        }
    }.start(wait = true)
}
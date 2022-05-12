package com.github.neckbosov.bsc_bosov.server

import com.github.neckbosov.bsc_bosov.code_mapper.PythonMapper
import com.github.neckbosov.bsc_bosov.common.DeletedTask
import com.github.neckbosov.bsc_bosov.common.Task
import com.github.neckbosov.bsc_bosov.dsl.program.Program
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import com.github.neckbosov.bsc_bosov.dsl.tags.PythonTag
import com.github.neckbosov.bsc_bosov.dsl.template.dslModule
import com.github.neckbosov.bsc_bosov.server.dao.TemplateOps
import com.github.neckbosov.bsc_bosov.server.dao.VariantsOps
import com.github.neckbosov.bsc_bosov.server.dao.createMongoDB
import com.github.neckbosov.bsc_bosov.tasks.task
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.jetty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.coroutines.runBlocking
import kotlinx.html.*
import kotlinx.serialization.json.Json

private val mongoDB by lazy {
    runBlocking {
        createMongoDB()
    }
}

@Suppress("UNCHECKED_CAST")
private inline fun <reified LanguageTag : ProgramLanguageTag> castProgram(program: Program<out ProgramLanguageTag>): Program<LanguageTag> =
    program as Program<LanguageTag>

fun main() {
    val variantsDB = VariantsOps(mongoDB)
    val templatesDB = TemplateOps(mongoDB)
    embeddedServer(Jetty, port = 8080) {
        install(ContentNegotiation) {
            json(Json { serializersModule = dslModule })
        }
        routing {
            get("/get_source") {
                val task = call.request.queryParameters["task"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val seed =
                    call.request.queryParameters["seed"]?.toLong() ?: return@get call.respond(HttpStatusCode.BadRequest)
                val attributes = call.request.queryParameters.toMap()

                val program = task().fill(seed, attributes)
                val code = PythonMapper.generateCode(castProgram(program))
                var source = variantsDB.getText(attributes)
                var status = HttpStatusCode.OK
                if (source == null) {
                    //TODO:Change tag
                    val programData = generateProgramData(PythonTag, attributes, code, variantsDB)
                    source = programData.codeText
                    status = HttpStatusCode.Created
                }
                call.respond(status, source)
            }

            get("/get_image_bytes_png") {
                val task = call.request.queryParameters["task"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val seed =
                    call.request.queryParameters["seed"]?.toLong() ?: return@get call.respond(HttpStatusCode.BadRequest)
                val attributes = call.request.queryParameters.toMap()

                val program = task().fill(seed, attributes)
                val code = PythonMapper.generateCode(castProgram(program))
                var source = variantsDB.getImage(attributes)
                var status = HttpStatusCode.OK
                if (source == null) {
                    //TODO:Change tag
                    val programData = generateProgramData(PythonTag, attributes, code, variantsDB)
                    source = programData.image
                    status = HttpStatusCode.Created
                }
                call.respondBytes(source!!, status = status)
            }
            get("/get_image") {
                call.respondHtml {
                    body {
                        img(src = "/get_image_bytes_png?${call.request.queryString()}", alt = "Code image")
                    }
                }
            }
            get("/") {
                val taskNames = templatesDB.getAllTaskNames()
                call.respondHtml {
                    body {
                        div {
                            p {
                                +"""Для получения текста программы сделайте запрос вида /get_source?task=<task_id>&seed=<random_seed>&<params>..."""
                                br { }
                                +"""Для получения изображения программы сделайте запрос вида /get_image?task=<task_id>&seed=<random_seed>&<params>..."""
                                br { }
                                +"""Доступные задачи:"""

                            }
                            ul {
                                for (task in taskNames) {
                                    li { +task }
                                }
                            }
                        }
                    }
                }
            }
            post("/add_task") {
                val task = call.receive<Task>()
                templatesDB.addTemplate(task.taskName, task.programTemplate, task.tag)
                call.respond(HttpStatusCode.Created)
            }
            delete("/delete_task") {
                val deletedTask = call.receive<DeletedTask>()
                templatesDB.deleteTemplate(deletedTask.taskName)
                call.respond(HttpStatusCode.OK)
            }
        }
    }.start(wait = true)
}
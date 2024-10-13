package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(StatusPages) {
        exception<IllegalStateException> { call, cause ->
            call.respondText("App in illegal state as ${cause.message}")
        }
    }
    routing {
        get("/tasks") {
            call.respondText(
                contentType = ContentType.parse("text/html"),
                text = """
                    <h3>TODO:</h3>
                    <ol>
                        <li>A table of all the tasks</li>
                        <li>A form to submit new taks</li>
                    </ol>
                """.trimIndent()
            )
        }
    }
    routing {
        staticResources("/content", "mycontent")
        get("/") {
            call.respondText("Hello World!")
        }
        get("/error-test") {
            throw IllegalStateException("Too Busy")
        }
    }
    routing {
        get("/test1") {
            val text = "<h1>Hello From Ktor</h1>"
            val type = ContentType.parse("text/html")

            call.respondText(text, type)
        }
    }
}

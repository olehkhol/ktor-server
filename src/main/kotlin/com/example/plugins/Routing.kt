package com.example.plugins

import com.example.model.Priority
import com.example.model.TaskRepository
import com.example.model.tasksAsTable
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
        get("/tasks/byPriority/{priority}") {
            val priorityAsText = call.parameters["priority"]

            if (priorityAsText == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            try {
                val priority = Priority.valueOf(priorityAsText)
                val tasks = TaskRepository.tasksByPriority(priority)

                if (tasks.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.respondText(
                    contentType = ContentType.parse("text/html"),
                    text = tasks.tasksAsTable()
                )
            } catch (ex: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
    routing {
        get("/tasks") {
            val tasks = TaskRepository.allTasks()

            call.respondText(
                contentType = ContentType.parse("text/html"),
                text = tasks.tasksAsTable(),
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
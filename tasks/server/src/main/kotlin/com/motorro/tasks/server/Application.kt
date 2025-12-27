package com.motorro.tasks.server

import com.motorro.tasks.data.ErrorCode
import com.motorro.tasks.data.HttpResponse
import com.motorro.tasks.data.Task
import com.motorro.tasks.data.TaskId
import com.motorro.tasks.data.TaskUpdates
import com.motorro.tasks.data.Version
import com.motorro.tasks.data.VersionResponse
import com.motorro.tasks.data.httpResponseModule
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import java.util.UUID

fun main() {
    embeddedServer(
        Netty,
        host = SERVER_HOST,
        port = SERVER_PORT,
        module = Application::module
    ).start(wait = true)
}

private val taskUpdates = TaskUpdates(sequenceOf(
    Task(
        id = TaskId(UUID.randomUUID().toString()),
        author = USERNAME,
        title = "My first task",
        description = "Description 1",
        complete = false
    )
))

fun Application.module() {

    install(ContentNegotiation) {
        json(Json { serializersModule = httpResponseModule })
    }
    install(StatusPages) {
        exception<IllegalArgumentException> { call, cause ->
            call.respond<HttpResponse<Nothing>>(
                HttpStatusCode.BadRequest,
                HttpResponse.Error(ErrorCode.BAD_REQUEST, cause.message ?: ErrorCode.BAD_REQUEST.defaultMessage)
            )
        }
        exception<IllegalStateException> { call, cause ->
            call.respond<HttpResponse<Nothing>>(
                HttpStatusCode.Conflict,
                HttpResponse.Error(ErrorCode.CONFLICT, cause.message ?: ErrorCode.CONFLICT.defaultMessage)
            )
        }
        status(HttpStatusCode.Unauthorized) { call, cause ->
            call.respond<HttpResponse<Nothing>>(
                HttpStatusCode.Unauthorized,
                HttpResponse.Error(ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.defaultMessage)
            )
        }
        status(HttpStatusCode.Forbidden) { call, cause ->
            call.respond<HttpResponse<Nothing>>(
                HttpStatusCode.Forbidden,
                HttpResponse.Error(ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.defaultMessage)
            )
        }
        unhandled { call ->
            call.respond<HttpResponse<Nothing>>(
                HttpStatusCode.Conflict,
                HttpResponse.Error(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.defaultMessage)
            )
        }
    }
    install(Authentication) {
        stubBearer("client-area")
    }
    routing {
        get("/") {
            call.respond(HttpResponse.Data("Hello World"))
        }
        post("/login") {
            login()
        }
        authenticate("client-area") {
            get("/tasks/version") {
                call.respond<HttpResponse<VersionResponse>>(HttpResponse.Data(taskUpdates.getCurrentVersion()))
            }
            get("/tasks/updates") {
                call.respond<HttpResponse<TaskUpdates>>(HttpResponse.Data(taskUpdates.get(call.request.queryParameters["version"]?.let(::Version))))
            }
            post("/tasks/updates") {
                call.respond<HttpResponse<VersionResponse>>(HttpResponse.Data(taskUpdates.register(call.receive())))
            }
        }
    }
}
package com.motorro.network.server

import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json


fun main() {
    embeddedServer(
        Netty,
        host = SERVER_HOST,
        port = SERVER_PORT,
        module = {
            module(UsersImpl(profiles))
        }
    ).start(wait = true)
}

fun Application.module(users: Users) {

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
        })
    }
    install(StatusPages) {
        exception<IllegalArgumentException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.message ?: "Bad request")
        }
        exception<IllegalStateException> { call, cause ->
            call.respond(HttpStatusCode.Conflict, cause.message ?: "Conflict")
        }
        status(HttpStatusCode.Unauthorized) { call, _ ->
            call.respond(HttpStatusCode.Unauthorized, "Unauthorized")
        }
        status(HttpStatusCode.Forbidden) { call, _ ->
            call.respond(HttpStatusCode.Forbidden, "Forbidden")
        }
        exception<NotFoundException> { call, cause ->
            call.respond(HttpStatusCode.NotFound, cause.message ?: "Not found")
        }
        unhandled { call ->
            call.respond(HttpStatusCode.NotFound, "Not found")
        }
    }
    routing {
        openAPI(path="openapi", swaggerFile = "openapi/documentation.yaml")

        get("/users") {
            call.respond(users.getUsers())
        }

        get("/profiles/{id}") {
            call.respond(users.getProfile(call.parameters["id"]?.toIntOrNull() ?: throw IllegalArgumentException("Invalid user id")))
        }
    }
}
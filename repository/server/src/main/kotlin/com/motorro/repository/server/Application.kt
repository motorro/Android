package com.motorro.repository.server

import com.motorro.repository.data.Book
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import kotlin.uuid.Uuid


fun main() {
    embeddedServer(
        Netty,
        host = SERVER_HOST,
        port = SERVER_PORT,
        module = {
            module(BooksImpl(books, DELAY))
        }
    ).start(wait = true)
}

fun Application.module(books: Books) {

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
        exception<NotFoundException> { call, cause ->
            call.respond(HttpStatusCode.NotFound, cause.message ?: "Not found")
        }
        unhandled { call ->
            call.respond(HttpStatusCode.NotFound, "Not found")
        }
    }
    routing {
        get("/books") {
            call.respond(books.getBooks())
        }
        get("/books/{id}") {
            val id = kotlin.runCatching {
                Uuid.parse(requireNotNull(call.parameters["id"]))
            }.getOrElse {
                throw IllegalArgumentException("Invalid book id", it)
            }
            call.respond(books.getBook(id))
        }
        post("/books") {
            call.respond(books.addBook(call.receive<Book>()))
        }
    }
}
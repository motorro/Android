package com.motorro.repository.server

import com.motorro.repository.data.Book
import com.motorro.repository.data.ListBook
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant
import kotlin.uuid.Uuid

class ApplicationTest {

    private val json = Json

    private val book = Book(
        id = Uuid.parse("466ab2b7-3225-4706-910c-c40c80cbf5ae"),
        authors = listOf("Author"),
        title = "Title",
        cover = "Cover",
        summary = "Summary",
        datePublished = Instant.parse("2023-10-01T00:00:00Z")
    )

    private val newBook = book.copy(
        id = Uuid.parse("309f2c6b-298f-4c29-9b90-7fab1a549ee3")
    )

    private val books: Books = mockk{
        coEvery { getBooks() } returns listOf(book).map {
            ListBook(
                id = it.id,
                title = it.title,
                cover = it.cover
            )
        }

        coEvery { getBook(any()) } answers {
            val bookId = firstArg<Uuid>()
            if (bookId == book.id) {
                book
            } else {
                throw NotFoundException("Book with id $bookId not found")
            }
        }

        coEvery { addBook(any()) } answers {
            firstArg<Book>()
        }
    }

    private fun ApplicationTestBuilder.prepareClient(): HttpClient {
        return createClient {
            install(ContentNegotiation) {
                json(Json)
            }
        }
    }

    @Test
    fun returnsBookList() = testApplication {
        application {
            module(books)
        }
        val response = prepareClient().get("/books") {
            accept(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(
            listOf(book).map {
                ListBook(
                    id = it.id,
                    title = it.title,
                    cover = it.cover
                )
            },
            json.decodeFromString(response.bodyAsText())
        )
    }

    @Test
    fun returnsBook() = testApplication {
        application {
            module(books)
        }
        val response = prepareClient().get("/books/${book.id}") {
            accept(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(
            book,
            json.decodeFromString(response.bodyAsText())
        )
    }

    @Test
    fun failsIfBookNotFound() = testApplication {
        application {
            module(books)
        }
        val response = prepareClient().get("/books/5e22fc20-0cee-41c6-8bc2-b58cb4524d77") {
            accept(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun addsBook() = testApplication {
        application {
            module(books)
        }
        val response = prepareClient().post("/books") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(newBook)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        coVerify { books.addBook(newBook) }
        assertEquals(
            newBook,
            json.decodeFromString(response.bodyAsText())
        )
    }
}
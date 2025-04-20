package com.motorro.repository.server

import com.motorro.repository.data.Book
import com.motorro.repository.data.ListBook
import io.ktor.server.plugins.NotFoundException
import kotlin.time.Clock
import kotlin.uuid.Uuid

interface Books {
    suspend fun getBooks(): List<ListBook>
    suspend fun getBook(id: Uuid): Book
    suspend fun addBook(book: Book): Book
}

class BooksImpl(private var books: List<Book>, private val delay: Long = 0) : Books {

    override suspend fun getBooks(): List<ListBook> = delay {
        books.map {
            ListBook(
                id = it.id,
                title = it.title,
                cover = it.cover
            )
        }
    }

    override suspend fun getBook(id: Uuid): Book = delay {
        return books.find { it.id == id } ?: throw NotFoundException("Book with id $id not found")
    }

    override suspend fun addBook(book: Book): Book = delay {
        println(book)
        if (books.any { it.id == book.id }) {
            throw IllegalStateException("Book with id ${book.id} already exists")
        }
        books = books + book.copy(datePublished = Clock.System.now())
        return book
    }

    private suspend inline fun <R> delay(block: () -> R): R {
        if (delay > 0) {
            kotlinx.coroutines.delay(delay)
        }
        return block()
    }
}
package com.motorro.repository.usecase

import com.motorro.repository.data.Book
import com.motorro.repository.data.NewBook
import com.motorro.repository.net.BooksApi
import kotlin.time.Clock
import kotlin.uuid.Uuid

/**
 * Use case to add a book.
 */
interface AddBook {
    /**
     * Invokes the use case to create a new book.
     *
     * @param book New book to be added.
     */
    suspend operator fun invoke(book: NewBook)

    class Impl(private val booksApi: BooksApi) : AddBook {
        override suspend fun invoke(book: NewBook) {
            booksApi.addBook(Book(
                id = Uuid.random(),
                title = book.title,
                authors = book.authors,
                summary = book.summary,
                cover = book.cover,
                datePublished = Clock.System.now()
            )).getOrThrow()
        }
    }
}
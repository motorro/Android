package com.motorro.repository.usecase

import com.motorro.repository.data.NewBook
import com.motorro.repository.repository.BookRepository

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

    class Impl(private val bookRepository: BookRepository) : AddBook {
        override suspend fun invoke(book: NewBook) {
            bookRepository.addBook(book)
        }
    }
}
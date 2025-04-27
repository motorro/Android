package com.motorro.repository.net

import com.motorro.repository.data.Book
import com.motorro.repository.data.ListBook
import kotlin.uuid.Uuid

/**
 * Network API for book data
 */
interface BooksApi {
    /**
     * Returns a list of books
     */
    suspend fun getBookList(): Result<List<ListBook>>

    /**
     * Returns a full book data
     */
    suspend fun getBook(bookId: Uuid): Result<Book>

    /**
     * Adds a new book
     */
    suspend fun addBook(book: Book): Result<Book>
}
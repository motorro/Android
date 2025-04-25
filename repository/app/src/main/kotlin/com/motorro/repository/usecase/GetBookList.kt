package com.motorro.repository.usecase

import com.motorro.repository.data.BookListLceState
import com.motorro.repository.data.ListBook
import com.motorro.repository.repository.BookListRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case to get a list of books.
 */
interface GetBookList {
    /**
     * Invokes the use case to get a list of books.
     *
     * @return A state of loading a list of [ListBook]s.
     */
    operator fun invoke(): Flow<BookListLceState>

    /**
     * Refreshes the list of books.
     */
    suspend fun refresh()

    class Impl(private val booksRepository: BookListRepository) : GetBookList {
        override fun invoke(): Flow<BookListLceState> = booksRepository.bookList

        override suspend fun refresh() {
            booksRepository.synchronize()
        }
    }
}
package com.motorro.repository.usecase

import com.motorro.repository.data.BookLceState
import com.motorro.repository.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

/**
 * Use case to get a full book.
 */
interface GetBook {
    /**
     * Invokes the use case to get a profile.
     *
     * @param bookId The user ID to get the profile for.
     * @return A user profile.
     */
    operator fun invoke(bookId: Uuid): Flow<BookLceState>

    /**
     * Refreshes the book data.
     */
    suspend fun refresh()

    class Impl(private val bookRepository: BookRepository) : GetBook {
        override fun invoke(bookId: Uuid): Flow<BookLceState> = bookRepository.getBook(bookId)

        override suspend fun refresh() {
            bookRepository.synchronize()
        }
    }
}
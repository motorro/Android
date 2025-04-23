package com.motorro.repository.usecase

import com.motorro.repository.data.BookLceState
import com.motorro.repository.net.BooksApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
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

    @OptIn(ExperimentalCoroutinesApi::class)
    class Impl(private val booksApi: BooksApi) : GetBook {
        private val loader = createLceLoader { it }
        private val refresh = MutableSharedFlow<Unit>()

        override fun invoke(bookId: Uuid): Flow<BookLceState> = refresh.onStart { emit(Unit) }.flatMapLatest {
            loader.load {
                booksApi.getBook(bookId)
            }
        }

        override suspend fun refresh() {
            refresh.emit(Unit)
        }
    }
}
package com.motorro.repository.usecase

import com.motorro.repository.data.BookListLceState
import com.motorro.repository.data.ListBook
import com.motorro.repository.net.BooksApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart

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

    @OptIn(ExperimentalCoroutinesApi::class)
    class Impl(private val booksApi: BooksApi) : GetBookList {

        private val loader = createLceLoader { it }
        private val refresh = MutableSharedFlow<Unit>()

        override fun invoke(): Flow<BookListLceState> = refresh.onStart { emit(Unit) }.flatMapLatest {
            loader.load {
                booksApi.getBookList()
            }
        }

        override suspend fun refresh() {
            refresh.emit(Unit)
        }
    }
}
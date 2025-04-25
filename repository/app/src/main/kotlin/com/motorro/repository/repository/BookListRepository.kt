package com.motorro.repository.repository

import com.motorro.core.lce.LceState
import com.motorro.repository.data.BookListLceState
import com.motorro.repository.data.ListBook
import com.motorro.repository.db.dao.BooksDao
import com.motorro.repository.db.entity.toDomain
import com.motorro.repository.db.entity.toEntity
import com.motorro.repository.net.BooksApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * Book repository
 */
interface BookListRepository {
    /**
     * A list of books
     */
    val bookList: Flow<BookListLceState>

    /**
     * Explicitly synchronizes local data with server
     */
    suspend fun synchronize()

    @OptIn(ExperimentalCoroutinesApi::class)
    class Impl(private val booksDao: BooksDao, private val booksApi: BooksApi) : BookListRepository {

        // Manual refresh flow
        private val refresh = MutableSharedFlow<Unit>()

        override val bookList: Flow<BookListLceState> =
            // 1. Listen for DB changes
            booksDao.getBooks()
            .map { fromDb -> fromDb.map { it.toDomain() } }
            .flatMapLatest { fromDb -> flow {
                // 2. Emit local content
                emit(LceState.Content(fromDb))

                var starter: Flow<Unit> = refresh
                if (fromDb.isEmpty()) {
                    // 3. If the list is empty - we will start refresh immediately
                    starter = refresh.onStart { emit(Unit) }
                }

                // 4. Update from network when refresh is requested
                emitAll(starter.flatMapLatest { updateFromNetwork(fromDb.takeIf { it.isNotEmpty() })})
            }}

        /**
         * Updates local data from network
         */
        private fun updateFromNetwork(soFar: List<ListBook>?): Flow<BookListLceState> = flow {
            // 1. Show loading
            emit(LceState.Loading(soFar))
            val fromNetwork = booksApi.getBookList()
            if (fromNetwork.isSuccess) {
                val books = fromNetwork.getOrDefault(emptyList())
                // 2. Save locally if successfully updated
                booksDao.insertBooks(books.map { it.toEntity() })
            } else {
                // 3. Display error
                emit(LceState.Error(
                    error = fromNetwork.exceptionOrNull() ?: RuntimeException("Unknown error"),
                    soFar
                ))
            }
        }

        override suspend fun synchronize() {
            refresh.emit(Unit)
        }
    }
}
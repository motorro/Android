package com.motorro.repository.repository

import com.motorro.core.lce.LceState
import com.motorro.repository.data.Book
import com.motorro.repository.data.BookLceState
import com.motorro.repository.db.dao.BooksDao
import com.motorro.repository.db.entity.hasBookInfo
import com.motorro.repository.db.entity.toDomain
import com.motorro.repository.db.entity.toEntity
import com.motorro.repository.net.BooksApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlin.uuid.Uuid

/**
 * Book repository
 */
interface BookRepository {
    /**
     * Gets book data
     * @param id Book ID
     */
    fun getBook(id: Uuid): Flow<BookLceState>

    /**
     * Explicitly synchronizes local data with server
     */
    suspend fun synchronize()


    @OptIn(ExperimentalCoroutinesApi::class)
    class Impl(private val booksDao: BooksDao, private val booksApi: BooksApi) : BookRepository {

        // Manual refresh flow
        private val refresh = MutableSharedFlow<Unit>()

        override fun getBook(id: Uuid): Flow<BookLceState> =
            // 1. Listen for DB changes
            booksDao.getBook(id.toString()).flatMapLatest { fromDb ->
                flow {
                    val book = fromDb.firstOrNull()

                    var starter: Flow<Unit> = refresh
                    if (null != book) {
                        // 2. Emit complete or partial content
                        emit(LceState.Content(fromDb.first().toDomain()))
                    }
                    if (null == book || book.hasBookInfo.not()) {
                        starter = refresh.onStart { emit(Unit) }
                    }

                    // 3. Update from network when refresh is requested
                    emitAll(starter.flatMapLatest { updateFromNetwork(book?.toDomain(), id) })
                }
            }

        /**
         * Updates local data from network
         */
        private fun updateFromNetwork(soFar: Book?, bookId: Uuid): Flow<BookLceState> = flow {
            // 1. Show loading
            emit(LceState.Loading(soFar))
            val fromNetwork = booksApi.getBook(bookId)
            if (fromNetwork.isSuccess) {
                val book = fromNetwork.getOrThrow()
                // 2. Save locally if successfully updated
                booksDao.insertBook(book.toEntity())
            } else {
                // 3. Display error
                emit(
                    LceState.Error(
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
package com.motorro.repository.usecase

import android.util.Log
import com.motorro.repository.db.dao.BooksDao
import com.motorro.repository.db.entity.toDomain
import com.motorro.repository.net.BooksApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid

/**
 * Background Worker to add a book.
 */
interface AddBookWorker {
    /**
     * Schedules a book to be added.
     *
     * @param bookId ID of the book to be added.
     */
    fun schedule(bookId: Uuid)

    class Impl(
        private val booksApi: BooksApi,
        private val booksDao: BooksDao
    ) : AddBookWorker {

        private val schedulerScope = CoroutineScope(SupervisorJob())

        override fun schedule(bookId: Uuid) {
            schedulerScope.launch {
                Log.i(TAG, "Scheduling saving book with ID: $bookId")
                val book = booksDao.getBook(bookId.toString()).firstOrNull()?.firstOrNull()
                if (null == book) {
                    Log.e(TAG, "Book with ID: $bookId not found in DB")
                    return@launch
                }
                Log.i(TAG, "Book with ID: $bookId found in DB, adding to API")
                val result = booksApi.addBook(book.toDomain())
                if (result.isSuccess) {
                    Log.i(TAG, "Book with ID: $bookId added successfully")
                } else {
                    Log.e(TAG, "Failed to add book with ID: $bookId")
                }
            }
        }

        companion object {
            private const val TAG = "AddBookWorker"
        }
    }
}
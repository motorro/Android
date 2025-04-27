package com.motorro.repository.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.motorro.repository.data.NewBook
import com.motorro.repository.db.entity.BookInfoEntity
import com.motorro.repository.db.entity.CompleteBook
import com.motorro.repository.db.entity.ListBookEntity
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock
import kotlin.uuid.Uuid

/**
 * Books DAO
 */
@Dao
abstract class BooksDao {
    @Query("SELECT id, title, cover FROM books")
    abstract fun getBooks(): Flow<List<ListBookEntity>>

    @Upsert
    abstract suspend fun insertBooks(books: List<ListBookEntity>)

    @Transaction
    @Query("SELECT * FROM books WHERE id=:id")
    abstract fun getBook(id: String): Flow<List<CompleteBook>>

    @Upsert
    abstract suspend fun insertBook(book: ListBookEntity)

    @Upsert
    abstract suspend fun insertBookInfo(book: BookInfoEntity)

    @Transaction
    open suspend fun insertBook(book: CompleteBook) {
        insertBook(book.listBook)
        insertBookInfo(requireNotNull(book.info) { "Complete book info is required" } )
    }

    @Transaction
    open suspend fun insertBook(book: NewBook): Uuid {
        val bookId = Uuid.random()
        insertBook(ListBookEntity(
            id = bookId,
            title = book.title,
            cover = book.cover
        ))
        insertBookInfo(BookInfoEntity(
            id = bookId,
            authors = book.authors,
            summary = book.summary,
            datePublished = Clock.System.now()
        ))
        return bookId
    }
}
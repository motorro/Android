package com.motorro.repository.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.motorro.repository.db.entity.ListBookEntity
import kotlinx.coroutines.flow.Flow

/**
 * Books DAO
 */
@Dao
interface BooksDao {
    @Query("SELECT id, title, cover FROM books")
    fun getBooks(): Flow<List<ListBookEntity>>

    @Upsert
    suspend fun insertBooks(books: List<ListBookEntity>)
}
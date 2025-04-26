package com.motorro.repository.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.motorro.repository.db.dao.BooksDao
import com.motorro.repository.db.entity.BookInfoEntity
import com.motorro.repository.db.entity.ListBookEntity

/**
 * Local books data source
 */
@Database(
    entities = [ListBookEntity::class, BookInfoEntity::class],
    version = 2,
    autoMigrations = [
        AutoMigration(1, 2)
    ]
)
@TypeConverters(Converters::class)
abstract class BooksDb : RoomDatabase() {
    /**
     * Books DAO
     */
    abstract fun getBooksDao(): BooksDao

    companion object {
        fun create(context: Context) = Room
            .databaseBuilder(context, BooksDb::class.java, "books.db")
            .build()
    }
}
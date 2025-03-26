package com.motorro.sqlite.data

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

/**
 * Image data
 * @param path Image path
 * @param name Image name
 * @param dateTimeTaken Date and time image was taken
 */
@Entity(tableName = Image.TABLE_NAME)
data class Image(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_PATH)
    val path: Uri,
    @ColumnInfo(name = COLUMN_NAME, index = true)
    val name: String,
    @ColumnInfo(name = COLUMN_CREATED, index = true)
    val dateTimeTaken: LocalDateTime
) {
    companion object {
        const val TABLE_NAME = "photo"
        const val COLUMN_PATH = "path"
        const val COLUMN_NAME = "name"
        const val COLUMN_CREATED = "created"
    }
}
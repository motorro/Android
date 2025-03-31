package com.motorro.sqlite.data

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

/**
 * Image data
 * @param path Image path
 * @param name Image name
 * @param dateTimeTaken Date and time image was taken
 */
@Entity(
    tableName = Image.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Tag::class,
            parentColumns = [Tag.COLUMN_ID],
            childColumns = [Image.COLUMN_TAG],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = [Image.COLUMN_CREATED], orders = [Index.Order.DESC]),
    ]
)
data class Image(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_PATH)
    val path: Uri,
    @ColumnInfo(name = COLUMN_NAME, index = true)
    val name: String,
    @ColumnInfo(name = COLUMN_CREATED)
    val dateTimeTaken: LocalDateTime,
    @ColumnInfo(name = COLUMN_TAG, index = true)
    val tag: Int? = null
) {
    companion object {
        const val TABLE_NAME = "photo"
        const val COLUMN_PATH = "path"
        const val COLUMN_NAME = "name"
        const val COLUMN_CREATED = "created"
        const val COLUMN_TAG = Tag.TABLE_NAME
    }
}
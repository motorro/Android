package com.motorro.sqlite.data

import android.net.Uri
import androidx.room.ColumnInfo
import kotlinx.datetime.LocalDateTime

/**
 * List image data
 * @param path Image path
 * @param name Image name
 * @param dateTimeTaken Date and time image was taken
 */
data class ListImage(
    @ColumnInfo(name = Image.COLUMN_PATH)
    val path: Uri,
    @ColumnInfo(name = Image.COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = Image.COLUMN_CREATED)
    val dateTimeTaken: LocalDateTime
)
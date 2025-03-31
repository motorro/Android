package com.motorro.sqlite.data

import android.net.Uri
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.datetime.LocalDateTime

/**
 * List image data
 * @param path Image path
 * @param name Image name
 * @param dateTimeTaken Date and time image was taken
 */
data class ListImage(
    @Embedded
    private val imageData: Image,
    @Relation(parentColumn = Image.COLUMN_TAG, entityColumn = Tag.COLUMN_ID)
    private val tagData: Tag?
) {
    val path: Uri get() = imageData.path
    val name: String get() = imageData.name
    val dateTimeTaken: LocalDateTime get() = imageData.dateTimeTaken
    val tags: List<String> get() = listOfNotNull(tagData?.name)
}

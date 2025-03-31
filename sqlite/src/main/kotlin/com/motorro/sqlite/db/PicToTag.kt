package com.motorro.sqlite.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.motorro.sqlite.data.Image
import com.motorro.sqlite.data.Tag

/**
 * Pic to tag cross-reference
 */
@Entity(
    tableName = PicToTag.TABLE_NAME,
    primaryKeys = [PicToTag.COLUMN_IMAGE_PATH, PicToTag.COLUMN_TAG_ID],
    foreignKeys = [
        ForeignKey(
            entity = Image::class,
            parentColumns = [Image.COLUMN_PATH],
            childColumns = [PicToTag.COLUMN_IMAGE_PATH],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Tag::class,
            parentColumns = [Tag.COLUMN_ID],
            childColumns = [PicToTag.COLUMN_TAG_ID],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class PicToTag(
    @ColumnInfo(name = COLUMN_IMAGE_PATH, index = true)
    val imagePath: String,
    @ColumnInfo(name = COLUMN_TAG_ID, index = true)
    val tagId: Int
) {
    companion object {
        const val TABLE_NAME = "pic_to_tag"
        const val COLUMN_IMAGE_PATH = "image_path"
        const val COLUMN_TAG_ID = "tag_id"
    }
}
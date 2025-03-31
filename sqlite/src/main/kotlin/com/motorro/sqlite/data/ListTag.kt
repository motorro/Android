package com.motorro.sqlite.data

import androidx.room.ColumnInfo

/**
 * List tag data
 * @param id Image path
 * @param name Image name
 */
data class ListTag(
    @ColumnInfo(name = Tag.COLUMN_ID)
    val id: Int,
    @ColumnInfo(name = Tag.COLUMN_NAME)
    val name: String
)
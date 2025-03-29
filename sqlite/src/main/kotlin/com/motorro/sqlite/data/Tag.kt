package com.motorro.sqlite.data

import androidx.room.ColumnInfo

/**
 * Tag data
 * @property name Tag name
 * @property description Tag description
 */
data class Tag(
    @ColumnInfo(name = COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = COLUMN_DESCRIPTION)
    val description: String
) {
    companion object {
        const val TABLE_NAME = "tag"
        const val COLUMN_NAME = "name"
        const val COLUMN_DESCRIPTION = "description"
    }
}
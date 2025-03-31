package com.motorro.sqlite.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Tag data
 * @property name Tag name
 * @property description Tag description
 */
@Entity(
    tableName = Tag.TABLE_NAME,
    indices = [
        Index(value = [Tag.COLUMN_NAME], unique = true)
    ]
)
data class Tag(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    val id: Int,
    @ColumnInfo(name = COLUMN_NAME, collate = ColumnInfo.NOCASE)
    val name: String,
    @ColumnInfo(name = COLUMN_DESCRIPTION)
    val description: String,
) {
    companion object {
        const val TABLE_NAME = "tag"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_DESCRIPTION = "description"
    }
}
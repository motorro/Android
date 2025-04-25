package com.motorro.repository.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.motorro.repository.data.ListBook
import kotlin.uuid.Uuid

@Entity(tableName = "books")
data class ListBookEntity(
    @PrimaryKey
    val id: Uuid,
    val title: String,
    val cover: String
)

fun ListBook.toEntity() = ListBookEntity(
    id = id,
    title = title,
    cover = cover
)

fun ListBookEntity.toDomain() = ListBook(
    id = id,
    title = title,
    cover = cover
)
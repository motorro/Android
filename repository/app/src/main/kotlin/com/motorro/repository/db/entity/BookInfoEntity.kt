package com.motorro.repository.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.motorro.repository.data.Book
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity(
    tableName = "book_info",
    foreignKeys = [
        ForeignKey(
            ListBookEntity::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            deferred = true
        )
    ]
)
data class BookInfoEntity(
    @PrimaryKey
    val id: Uuid,
    val authors: List<String>,
    val summary: String,
    val datePublished: Instant
)

data class CompleteBook(
    @Embedded
    val listBook: ListBookEntity,
    @Relation(parentColumn = "id", entityColumn = "id")
    val info: BookInfoEntity?
)

val CompleteBook.hasBookInfo: Boolean get() = null != info

fun CompleteBook.toDomain() = Book(
    id = listBook.id,
    authors = info?.authors.orEmpty(),
    title = listBook.title,
    cover = listBook.cover,
    summary = info?.summary.orEmpty(),
    datePublished = Instant.DISTANT_FUTURE
)

fun Book.toEntity() = CompleteBook(
    listBook = ListBookEntity(
        id = id,
        title = title,
        cover = cover
    ),
    info = BookInfoEntity(
        id = id,
        authors = authors,
        summary = summary,
        datePublished = datePublished
    )
)
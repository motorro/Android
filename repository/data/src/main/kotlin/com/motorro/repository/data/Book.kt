package com.motorro.repository.data

import com.motorro.repository.data.serializer.SerializableUuid
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class Book(
    val id: SerializableUuid,
    val authors: List<String>,
    val title: String,
    val cover: String,
    val summary: String,
    val datePublished: Instant
)

package com.motorro.repository.data

import com.motorro.repository.data.serializer.SerializableUuid
import kotlinx.serialization.Serializable

@Serializable
data class ListBook(
    val id: SerializableUuid,
    val title: String,
    val cover: String
)
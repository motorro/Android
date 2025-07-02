@file:UseSerializers(InstantSerializer::class)

package com.motorro.cookbook.model

import com.motorro.cookbook.model.serializer.InstantSerializer
import com.motorro.cookbook.model.serializer.SerializableUuid
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlin.time.Instant

@Serializable
data class ListRecipe(
    val id: SerializableUuid,
    val title: String,
    val category: RecipeCategory,
    val image: Image?,
    val dateTimeCreated: Instant,
    val deleted: Boolean = false
)
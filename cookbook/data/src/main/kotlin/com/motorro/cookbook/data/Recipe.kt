@file:UseSerializers(InstantSerializer::class)

package com.motorro.cookbook.data

import com.motorro.cookbook.data.serializer.InstantSerializer
import com.motorro.cookbook.data.serializer.SerializableUuid
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlin.time.Instant

/**
 * Recipe
 */
@Serializable
data class Recipe(
    val id: SerializableUuid,
    val title: String,
    val category: RecipeCategory,
    val image: Image?,
    val description: String,
    val dateTimeCreated: Instant,
    val deleted: Boolean = false
)
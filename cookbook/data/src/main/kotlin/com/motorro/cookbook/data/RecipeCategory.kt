package com.motorro.cookbook.data

import kotlinx.serialization.Serializable

/**
 * Recipe category data class
 */
@JvmInline
@Serializable
value class RecipeCategory(val name: String): Comparable<RecipeCategory> {
    override fun compareTo(other: RecipeCategory): Int = name.compareTo(other.name)
}
package com.motorro.cookbook.appcore.navigation

import kotlinx.serialization.Serializable

/**
 * Navigation destinations
 */
@Serializable
sealed interface Destination {
    /**
     * Add recipe destination
     */
    @Serializable
    data object AddRecipeDestination : Destination

    /**
     * Login destination
     */
    @Serializable
    data object LoginDestination : Destination

    /**
     * Recipe destination
     */
    @Serializable
    data class RecipeDestination(val id: String? = null) : Destination

    /**
     * Recipe list destination
     */
    @Serializable
    data object RecipeListDestination : Destination
}
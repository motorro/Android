package com.motorro.cookbook.appcore.navigation

import kotlinx.serialization.Serializable

/**
 * Navigation destinations
 */
@Serializable
sealed interface Destination {
    /**
     * Login destination
     */
    @Serializable
    data object LoginDestination : Destination

    /**
     * Recipe list destination
     */
    @Serializable
    data object RecipeListDestination : Destination
}
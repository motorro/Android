package com.motorro.cookbook.appcore.di

import com.motorro.cookbook.appcore.navigation.Links
import com.motorro.cookbook.domain.recipes.RecipeRepository
import com.motorro.cookbook.domain.session.SessionManager

/**
 * Provides dependencies
 */
interface DiContainer {
    /**
     * Session manager
     */
    val sessionManager: SessionManager

    /**
     * Session manager
     */
    val recipeRepository: RecipeRepository

    /**
     * Application deep-links
     */
    val links: Links
}
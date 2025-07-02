package com.motorro.cookbook.appcore.navigation

import androidx.navigation.NavDeepLinkRequest
import kotlin.uuid.Uuid

/**
 * Application deep-links
 */
interface Links {
    /**
     * Login screen deep-link
     */
    fun login(): NavDeepLinkRequest

    /**
     * Recipe deep-link
     * @param id Recipe ID
     */
    fun recipe(id: Uuid): NavDeepLinkRequest

    /**
     * Add recipe
     */
    fun addRecipe(): NavDeepLinkRequest
}
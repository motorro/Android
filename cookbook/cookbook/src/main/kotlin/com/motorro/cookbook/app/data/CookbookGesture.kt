package com.motorro.cookbook.app.data

import com.motorro.cookbook.recipelist.data.RecipeListGesture

/**
 * Application gesture
 */
sealed class CookbookGesture {
    /**
     * Back pressed
     */
    data object Back : CookbookGesture()

    /**
     * Cookbook flow
     */
    data class RecipeListFlow(val child: RecipeListGesture) : CookbookGesture()
}

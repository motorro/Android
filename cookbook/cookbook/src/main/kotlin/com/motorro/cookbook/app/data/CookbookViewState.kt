package com.motorro.cookbook.app.data

import com.motorro.cookbook.recipelist.data.RecipeListViewState

/**
 * Application view-state
 */
sealed class CookbookViewState {
    /**
     * Splash-screen
     */
    data object Loading : CookbookViewState()

    /**
     * Cookbook flow
     */
    data class RecipeListFlow(val child: RecipeListViewState) : CookbookViewState()

    /**
     * End of flow
     */
    data object Terminated : CookbookViewState()
}

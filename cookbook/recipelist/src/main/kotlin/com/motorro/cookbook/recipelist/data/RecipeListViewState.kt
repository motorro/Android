package com.motorro.cookbook.recipelist.data

import com.motorro.cookbook.addrecipe.data.AddRecipeViewState
import com.motorro.cookbook.appcore.navigation.auth.AuthViewState
import com.motorro.cookbook.recipe.data.RecipeViewState

/**
 * Recipe-list screen flow view-state
 */
sealed class RecipeListViewState {

    /**
     * Loading state
     */
    data object Loading : RecipeListViewState()

    /**
     * Recipe
     * @property state Recipe LCE state
     * @property addEnabled Add button enabled state
     * @property refreshEnabled Refresh button enabled state
     */
    data class Content(
        val state: RecipeListItemLce,
        val addEnabled: Boolean,
        val refreshEnabled: Boolean
    ) : RecipeListViewState()

    /**
     * Proxies add-recipe flow
     */
    data class AddRecipe(val child: AddRecipeViewState) : RecipeListViewState()

    /**
     * Proxies recipe flow
     */
    data class Recipe(val child: RecipeViewState) : RecipeListViewState()

    /**
     * Proxies authentication flow
     */
    data class Auth(val child: AuthViewState) : RecipeListViewState()

    companion object {
        /**
         * Empty initial state
         */
        val EMPTY = Loading
    }
}

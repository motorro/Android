package com.motorro.cookbook.recipe.data

import com.motorro.cookbook.appcore.navigation.auth.AuthViewState
import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.domain.recipes.data.RecipeLce
import com.motorro.cookbook.model.Recipe

/**
 * Recipe screen flow view-state
 */
sealed class RecipeViewState {
    /**
     * Recipe
     * @property state Recipe LCE state
     * @property deleteEnabled Delete button enabled state
     */
    data class Content(val state: RecipeLce, val deleteEnabled: Boolean) : RecipeViewState()

    /**
     * Delete confirmation
     * @property data Recipe to delete
     */
    data class DeleteConfirmation(val data: Recipe) : RecipeViewState()

    /**
     * Proxies authentication flow
     */
    data class Auth(val child: AuthViewState) : RecipeViewState()

    companion object {
        /**
         * Empty initial state
         */
        val EMPTY = Content(LceState.Loading(null), false)
    }
}

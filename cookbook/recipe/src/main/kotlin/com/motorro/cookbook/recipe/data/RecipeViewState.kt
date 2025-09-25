package com.motorro.cookbook.recipe.data

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
     * Terminated state - use to close the screen
     */
    data object Terminated : RecipeViewState()

    companion object {
        /**
         * Empty initial state
         */
        val EMPTY = Content(LceState.Loading(null), false)
    }
}

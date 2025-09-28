package com.motorro.cookbook.recipedemo.data

import com.motorro.cookbook.recipe.data.RecipeViewState

sealed class RecipeDemoViewState {
    /**
     * Starter state with a button
     */
    data object Starter : RecipeDemoViewState()

    /**
     * Recipe flow
     */
    data class RecipeFlow(val child: RecipeViewState) : RecipeDemoViewState()

    /**
     * Terminated (end of flow)
     */
    data object Terminated : RecipeDemoViewState()
}

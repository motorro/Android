package com.motorro.cookbook.recipedemo.data

import com.motorro.cookbook.recipe.data.RecipeGesture

sealed class RecipeDemoGesture {
    data object Back : RecipeDemoGesture()

    data object ToRecipe : RecipeDemoGesture()

    /**
     * Recipe flow
     */
    data class RecipeFlow(val child: RecipeGesture) : RecipeDemoGesture()
}
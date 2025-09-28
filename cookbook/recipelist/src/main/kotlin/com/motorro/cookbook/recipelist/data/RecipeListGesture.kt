package com.motorro.cookbook.recipelist.data

import com.motorro.cookbook.addrecipe.data.AddRecipeGesture
import com.motorro.cookbook.recipe.data.RecipeGesture
import kotlin.uuid.Uuid

/**
 * Gestures used by the Recipe-list screen flow
 */
sealed class RecipeListGesture {
    data object Back : RecipeListGesture()
    data object Logout : RecipeListGesture()
    data object Refresh : RecipeListGesture()
    data object DismissError : RecipeListGesture()

    /**
     * Add recipe button clicked
     */
    data object AddRecipeClicked : RecipeListGesture()

    /**
     * Proxies add-recipe flow
     */
    data class AddRecipeFlow(val child: AddRecipeGesture) : RecipeListGesture()

    /**
     * Recipe clicked
     */
    data class RecipeClicked(val id: Uuid) : RecipeListGesture()

    /**
     * Proxies recipe flow
     */
    data class RecipeFlow(val child: RecipeGesture) : RecipeListGesture()
}
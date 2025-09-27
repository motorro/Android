package com.motorro.cookbook.recipelist.data

/**
 * Gestures used by the Recipe-list screen flow
 */
sealed class RecipeListGesture {
    data object Back : RecipeListGesture()
    data object Logout : RecipeListGesture()
    data object Refresh : RecipeListGesture()
    data object DismissError : RecipeListGesture()
}
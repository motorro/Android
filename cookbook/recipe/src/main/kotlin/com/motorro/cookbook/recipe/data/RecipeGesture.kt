package com.motorro.cookbook.recipe.data

/**
 * Gestures used by the Recipe screen flow
 */
sealed class RecipeGesture {
    data object Back : RecipeGesture()
    data object DismissError : RecipeGesture()
    data object Delete : RecipeGesture()
    data object ConfirmDelete : RecipeGesture()
    data object CancelDelete : RecipeGesture()
}
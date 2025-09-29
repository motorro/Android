package com.motorro.cookbook.addrecipe.data

/**
 * Add recipe flow gesture
 */
sealed class AddRecipeGesture {
    data object Back : AddRecipeGesture()
    data class SetTitle(val title: String) : AddRecipeGesture()
    data class SetImage(val image: String?) : AddRecipeGesture()
    data class SetCategory(val category: String) : AddRecipeGesture()
    data class SetDescription(val description: String) : AddRecipeGesture()
    data object Save : AddRecipeGesture()
}
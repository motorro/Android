package com.motorro.cookbook.addrecipe.data

import androidx.compose.runtime.Immutable

/**
 * Add recipe flow view state
 */
sealed class AddRecipeViewState {
    /**
     * Add recipe form view state
     * @property title recipe title
     * @property image recipe image
     * @property category recipe category
     * @property description recipe description
     * @property saving true if recipe is saving
     */
    @Immutable
    data class Form(
        val title: String,
        val image: String?,
        val category: String,
        val categories: List<String>,
        val description: String,
        val isValid: Boolean,
        val saving: Boolean
    ) : AddRecipeViewState()

    data object Terminated : AddRecipeViewState()

    companion object {
        /**
         * Empty state
         */
        val EMPTY = Form(
            title = "",
            image = null,
            category = "",
            categories = emptyList(),
            description = "",
            isValid = false,
            saving = false
        )
    }
}
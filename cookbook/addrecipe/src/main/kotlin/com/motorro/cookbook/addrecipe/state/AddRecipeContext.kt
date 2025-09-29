package com.motorro.cookbook.addrecipe.state

import androidx.lifecycle.SavedStateHandle

/**
 * Common recipe flow dependencies
 */
internal interface AddRecipeContext {
    /**
     * State factory
     */
    val factory: AddRecipeStateFactory

    /**
     * Saved state handle
     */
    val savedStateHandle: SavedStateHandle
}
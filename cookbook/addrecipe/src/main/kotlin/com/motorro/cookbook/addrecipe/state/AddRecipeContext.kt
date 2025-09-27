package com.motorro.cookbook.addrecipe.state

import androidx.lifecycle.SavedStateHandle
import com.motorro.cookbook.appcore.navigation.CommonFlowHost

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

    /**
     * Flow host
     */
    val flowHost: CommonFlowHost
}
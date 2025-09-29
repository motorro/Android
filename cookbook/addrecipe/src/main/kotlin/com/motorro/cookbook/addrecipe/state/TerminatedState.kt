package com.motorro.cookbook.addrecipe.state

import com.motorro.cookbook.addrecipe.data.AddRecipeViewState
import com.motorro.cookbook.addrecipe.data.cleanup

/**
 * End of flow
 */
internal class TerminatedState(context: AddRecipeContext) : AddRecipeState(context) {
    override fun doStart() {
        super.doStart()
        savedStateHandle.cleanup()
        // At the moment we don't care about the result, so we just emit the final state
        setUiState(AddRecipeViewState.Terminated)
    }
}
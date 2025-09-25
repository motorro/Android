package com.motorro.cookbook.recipe.state

import com.motorro.cookbook.recipe.data.RecipeViewState

/**
 * End of flow
 */
internal class TerminatedState(context: RecipeContext) : RecipeState(context) {
    override fun doStart() {
        super.doStart()
        // At the moment we don't care about the result, so we just emit the final state
        setUiState(RecipeViewState.Terminated)
    }
}
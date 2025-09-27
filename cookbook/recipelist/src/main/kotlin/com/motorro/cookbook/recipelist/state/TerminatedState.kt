package com.motorro.cookbook.recipelist.state

import com.motorro.cookbook.recipelist.data.RecipeListViewState

/**
 * End of flow
 */
internal class TerminatedState(context: RecipeListContext) : RecipeListState(context) {
    override fun doStart() {
        super.doStart()
        // At the moment we don't care about the result, so we just emit the final state
        setUiState(RecipeListViewState.Terminated)
    }
}
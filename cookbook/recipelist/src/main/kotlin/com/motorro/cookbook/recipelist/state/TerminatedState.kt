package com.motorro.cookbook.recipelist.state

/**
 * End of flow
 */
internal class TerminatedState(context: RecipeListContext) : RecipeListState(context) {
    override fun doStart() {
        super.doStart()
        flowHost.onComplete()
    }
}
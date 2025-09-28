package com.motorro.cookbook.recipe.state

/**
 * End of flow
 */
internal class TerminatedState(context: RecipeContext) : RecipeState(context) {
    override fun doStart() {
        super.doStart()
        flowHost.onComplete()
    }
}
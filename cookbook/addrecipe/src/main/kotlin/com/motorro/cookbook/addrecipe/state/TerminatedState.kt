package com.motorro.cookbook.addrecipe.state

import com.motorro.cookbook.addrecipe.data.cleanup

/**
 * End of flow
 */
internal class TerminatedState(context: AddRecipeContext) : AddRecipeState(context) {
    override fun doStart() {
        super.doStart()
        savedStateHandle.cleanup()
        flowHost.onComplete()
    }
}
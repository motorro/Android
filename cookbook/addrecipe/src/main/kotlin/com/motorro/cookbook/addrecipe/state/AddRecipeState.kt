package com.motorro.cookbook.addrecipe.state

import androidx.annotation.CallSuper
import com.motorro.commonstatemachine.coroutines.CoroutineState
import com.motorro.cookbook.addrecipe.data.AddRecipeGesture
import com.motorro.cookbook.addrecipe.data.AddRecipeViewState
import com.motorro.cookbook.core.log.Logging

/**
 * Base recipe state
 */
internal abstract class AddRecipeState(context: AddRecipeContext) : CoroutineState<AddRecipeGesture, AddRecipeViewState>(), AddRecipeContext by context, Logging {

    @CallSuper
    override fun doStart() {
        d { "Starting..." }
    }

    override fun doProcess(gesture: AddRecipeGesture) {
        w { "Gesture $gesture is not supported" }
    }

    @CallSuper
    override fun doClear() {
        super.doClear()
        d { "Cleared" }
    }
}
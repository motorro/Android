package com.motorro.cookbook.recipe.state

import androidx.annotation.CallSuper
import com.motorro.commonstatemachine.coroutines.CoroutineState
import com.motorro.cookbook.core.log.Logging
import com.motorro.cookbook.recipe.data.RecipeGesture
import com.motorro.cookbook.recipe.data.RecipeViewState

/**
 * Base recipe state
 */
internal abstract class RecipeState(context: RecipeContext) : CoroutineState<RecipeGesture, RecipeViewState>(), RecipeContext by context, Logging {

    @CallSuper
    override fun doStart() {
        d { "Starting..." }
    }

    override fun doProcess(gesture: RecipeGesture) {
        w { "Gesture $gesture is not supported" }
    }

    @CallSuper
    override fun doClear() {
        super.doClear()
        d { "Cleared" }
    }
}
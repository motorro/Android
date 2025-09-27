package com.motorro.cookbook.recipelist.state

import androidx.annotation.CallSuper
import com.motorro.commonstatemachine.coroutines.CoroutineState
import com.motorro.cookbook.core.log.Logging
import com.motorro.cookbook.recipelist.data.RecipeListGesture
import com.motorro.cookbook.recipelist.data.RecipeListViewState

/**
 * Base recipe state
 */
internal abstract class RecipeListState(context: RecipeListContext) : CoroutineState<RecipeListGesture, RecipeListViewState>(), RecipeListContext by context, Logging {

    @CallSuper
    override fun doStart() {
        d { "Starting..." }
    }

    override fun doProcess(gesture: RecipeListGesture) {
        w { "Gesture $gesture is not supported" }
    }

    @CallSuper
    override fun doClear() {
        super.doClear()
        d { "Cleared" }
    }
}
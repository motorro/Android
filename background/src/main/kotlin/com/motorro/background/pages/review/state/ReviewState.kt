package com.motorro.background.pages.review.state

import androidx.annotation.CallSuper
import com.motorro.background.pages.review.data.ReviewGesture
import com.motorro.background.pages.review.data.ReviewUiState
import com.motorro.commonstatemachine.coroutines.CoroutineState
import com.motorro.core.log.Logging

abstract class ReviewState(context: ReviewContext): CoroutineState<ReviewGesture, ReviewUiState>(), ReviewContext by context, Logging {
    @CallSuper
    override fun doStart() {
        d { "Starting: ${this::class.java}..." }
    }

    override fun doProcess(gesture: ReviewGesture) {
        w { "Unsupported gesture: $gesture" }
    }

    @CallSuper
    override fun doClear() {
        super.doClear()
        d { "Cleared: ${this::class.java}" }
    }
}
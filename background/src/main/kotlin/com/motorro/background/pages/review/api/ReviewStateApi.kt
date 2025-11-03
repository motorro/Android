package com.motorro.background.pages.review.api

import com.motorro.background.api.MainScreenStateApi
import com.motorro.background.data.MainScreenGesture
import com.motorro.background.pages.review.data.ReviewGesture
import com.motorro.background.pages.review.data.ReviewUiState
import com.motorro.background.pages.review.state.FormState
import com.motorro.background.pages.review.state.ReviewStateFactory
import com.motorro.core.log.Logging
import javax.inject.Inject

/**
 * Flow API
 */
class ReviewStateApi @Inject constructor(private val stateFactory: ReviewStateFactory.Impl) : MainScreenStateApi<ReviewGesture, ReviewUiState>, Logging {
    override val data get() = ReviewPageData

    override fun init(data: Any?): FormState {
        d { "Starting Review flow..." }
        return stateFactory.form()
    }

    override fun getInitialViewState() = ReviewUiState.EMPTY

    override fun mapGesture(parent: MainScreenGesture): ReviewGesture? {
        return when {
            parent is MainScreenGesture.PageGesture && data == parent.page -> parent.gesture as ReviewGesture
            else -> null
        }
    }
}
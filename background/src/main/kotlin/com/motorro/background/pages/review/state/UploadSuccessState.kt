package com.motorro.background.pages.review.state

import com.motorro.background.pages.review.data.ReviewGesture
import com.motorro.background.pages.review.data.ReviewUiState
import com.motorro.background.pages.review.data.deleteReview

class UploadSuccessState(context: ReviewContext) : ReviewState(context) {
    override fun doStart() {
        super.doStart()
        savedStateHandle.deleteReview()
        setUiState(ReviewUiState.UploadSuccess)
    }

    override fun doProcess(gesture: ReviewGesture) {
        when(gesture) {
            is ReviewGesture.Action -> {
                d { "Complete..." }
                setMachineState(factory.form())
            }
            else -> super.doProcess(gesture)
        }
    }
}
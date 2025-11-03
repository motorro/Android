package com.motorro.background.pages.review.state

import com.motorro.background.pages.review.data.ReviewData
import com.motorro.background.pages.review.data.ReviewGesture
import com.motorro.background.pages.review.data.ReviewUiState
import com.motorro.background.pages.review.data.reviewData
import com.motorro.background.pages.review.data.updateReview
import kotlinx.coroutines.launch

class FormState(context: ReviewContext): ReviewState(context) {

    override fun doStart() {
        super.doStart()
        subscribeReview()
    }

    private fun subscribeReview() = stateScope.launch {
        savedStateHandle.reviewData.collect {
            render(it)
        }
    }

    override fun doProcess(gesture: ReviewGesture) {
        when(gesture) {
            is ReviewGesture.RatingChanged -> savedStateHandle.updateReview {
                copy(rating = gesture.rating)
            }
            is ReviewGesture.TextChanged -> savedStateHandle.updateReview {
                copy(text = gesture.text)
            }
            is ReviewGesture.PhotoAttached -> savedStateHandle.updateReview {
                copy(photos = photos + gesture.photo)
            }
            is ReviewGesture.PhotoRemoved -> savedStateHandle.updateReview {
                copy(photos = photos - gesture.photo)
            }
            ReviewGesture.Action -> {
                val reviewData = savedStateHandle.reviewData.value
                if (reviewData.isValid()) {
                    setMachineState(factory.uploading(reviewData))
                }
            }
        }
    }

    private fun ReviewData.isValid(): Boolean = rating in 1..5 && text.isNotBlank()

    private fun render(reviewData: ReviewData) {
        setUiState(ReviewUiState.Form(reviewData, reviewData.isValid()))
    }
}
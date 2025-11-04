package com.motorro.background.pages.review.data

sealed class ReviewUiState {

    data class Form(val data: ReviewData, val submitEnabled: Boolean) : ReviewUiState()

    data object UploadSuccess : ReviewUiState()

    companion object Companion {
        val EMPTY = Form(ReviewData(), false)
    }
}
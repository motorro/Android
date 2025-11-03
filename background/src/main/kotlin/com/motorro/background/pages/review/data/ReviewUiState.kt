package com.motorro.background.pages.review.data

sealed class ReviewUiState {

    data class Form(val data: ReviewData, val submitEnabled: Boolean) : ReviewUiState()

    data class Uploading(val progress: Pair<Int, Int>) : ReviewUiState()

    data object UploadSuccess : ReviewUiState()

    data class UploadFailed(val error: String) : ReviewUiState()

    companion object Companion {
        val EMPTY = Form(ReviewData(), false)
    }
}
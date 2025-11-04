package com.motorro.background.pages.review.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.background.pages.review.data.ReviewGesture
import com.motorro.background.pages.review.data.ReviewUiState

@Composable
fun ReviewScreen(state: ReviewUiState, onGesture: (ReviewGesture) -> Unit, modifier: Modifier = Modifier) {
    when(state) {
        is ReviewUiState.Form -> ReviewForm(
            state = state,
            onGesture = onGesture,
            modifier = modifier
        )
        ReviewUiState.UploadSuccess -> UploadSuccess(
            onClose = { onGesture(ReviewGesture.Action) }
        )
    }
}
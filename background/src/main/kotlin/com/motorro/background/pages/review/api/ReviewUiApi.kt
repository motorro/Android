package com.motorro.background.pages.review.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.background.api.MainScreenUiApi
import com.motorro.background.pages.review.data.ReviewUiState
import com.motorro.background.pages.review.ui.ReviewScreen
import javax.inject.Inject

/**
 * UI API
 */
class ReviewUiApi @Inject constructor(): MainScreenUiApi {
    override val data get() = ReviewPageData

    @Composable
    override fun Screen(state: Any, onGesture: (Any) -> Unit, modifier: Modifier) {
        ReviewScreen(
            state = state as ReviewUiState,
            onGesture = onGesture,
            modifier = modifier
        )
    }
}
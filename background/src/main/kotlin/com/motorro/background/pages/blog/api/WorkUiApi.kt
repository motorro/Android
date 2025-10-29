package com.motorro.background.pages.blog.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.background.api.MainScreenUiApi
import com.motorro.background.pages.blog.data.BlogUiState
import com.motorro.background.pages.blog.ui.ServiceScreen
import javax.inject.Inject

/**
 * UI API
 */
class WorkUiApi @Inject constructor(): MainScreenUiApi {
    override val data get() = BlogPageData

    @Composable
    override fun Screen(state: Any, onGesture: (Any) -> Unit, modifier: Modifier) {
        ServiceScreen(
            state = state as BlogUiState,
            onGesture = onGesture,
            modifier = modifier
        )
    }
}
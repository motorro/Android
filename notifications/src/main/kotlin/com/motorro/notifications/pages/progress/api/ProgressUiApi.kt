package com.motorro.notifications.pages.progress.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.notifications.api.MainScreenUiApi
import com.motorro.notifications.pages.progress.data.ProgressViewState
import com.motorro.notifications.pages.progress.ui.ProgressScreen
import javax.inject.Inject

/**
 * UI API
 */
class ProgressUiApi @Inject constructor(): MainScreenUiApi {
    override val data get() = ProgressPageData

    @Composable
    override fun Screen(state: Any, onGesture: (Any) -> Unit, modifier: Modifier) {
        ProgressScreen(
            state = state as ProgressViewState,
            onGesture = onGesture,
            modifier = modifier
        )
    }
}
package com.motorro.notifications.pages.push.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.notifications.api.MainScreenUiApi
import com.motorro.notifications.pages.push.data.PushViewState
import com.motorro.notifications.pages.push.ui.PushScreen
import javax.inject.Inject

/**
 * UI API
 */
class PushUiApi @Inject constructor(): MainScreenUiApi {
    override val data get() = PushPageData

    @Composable
    override fun Screen(state: Any, onGesture: (Any) -> Unit, modifier: Modifier) {
        PushScreen(
            state = state as PushViewState,
            modifier = modifier
        )
    }
}
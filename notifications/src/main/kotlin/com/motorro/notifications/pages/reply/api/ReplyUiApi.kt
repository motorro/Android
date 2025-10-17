package com.motorro.notifications.pages.reply.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.notifications.api.MainScreenUiApi
import com.motorro.notifications.pages.reply.data.ReplyViewState
import com.motorro.notifications.pages.reply.ui.ReplyScreen
import javax.inject.Inject

/**
 * UI API
 */
class ReplyUiApi @Inject constructor(): MainScreenUiApi {
    override val data get() = ReplyPageData

    @Composable
    override fun Screen(state: Any, onGesture: (Any) -> Unit, modifier: Modifier) {
        ReplyScreen(
            state = state as ReplyViewState,
            modifier = modifier
        )
    }
}
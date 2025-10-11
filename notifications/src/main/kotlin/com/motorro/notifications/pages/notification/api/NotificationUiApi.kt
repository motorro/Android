package com.motorro.notifications.pages.notification.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.notifications.api.MainScreenUiApi
import com.motorro.notifications.pages.notification.data.NotificationViewState
import com.motorro.notifications.pages.notification.ui.BasicNotificationScreen
import javax.inject.Inject

/**
 * UI API
 */
class NotificationUiApi @Inject constructor(): MainScreenUiApi {
    override val data get() = NotificationPageData

    @Composable
    override fun Screen(state: Any, onGesture: (Any) -> Unit, modifier: Modifier) {
        BasicNotificationScreen(
            state = state as NotificationViewState,
            onGesture = onGesture,
            modifier = modifier
        )
    }
}
package com.motorro.notifications.data

import androidx.compose.runtime.Immutable
import com.motorro.notifications.api.MainScreenPageData

sealed class MainScreenViewState {
    /**
     * Global loading
     */
    data object Loading : MainScreenViewState()

    /**
     * Requests to enable notifications
     */
    data object NeedToEnableNotifications : MainScreenViewState()

    /**
     * Main screen view state
     */
    @Immutable
    data class Page(val page: MainScreenPageData, val viewState: Any, val notificationAction: NotificationAction? = null) : MainScreenViewState()

    /**
     * End of flow
     */
    data object Terminated : MainScreenViewState()
}

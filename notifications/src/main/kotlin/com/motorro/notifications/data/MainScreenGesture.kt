package com.motorro.notifications.data

import com.motorro.notifications.api.MainScreenPageData

sealed class MainScreenGesture {
    data object Back : MainScreenGesture()
    data object Action : MainScreenGesture()

    data class NotificationPermissionRequested(val granted: Boolean) : MainScreenGesture()
    data object RecheckNotificationPermissions : MainScreenGesture()

    data class Navigate(val page: MainScreenPageData) : MainScreenGesture()
    data class PageGesture(val page: MainScreenPageData, val gesture: Any) : MainScreenGesture()
}
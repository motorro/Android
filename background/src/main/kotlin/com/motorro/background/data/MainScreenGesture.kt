package com.motorro.background.data

import com.motorro.background.api.MainScreenPageData

sealed class MainScreenGesture {
    data object Back : MainScreenGesture()

    data class NotificationPermissionRequested(val granted: Boolean) : MainScreenGesture()
    data object RecheckNotificationPermissions : MainScreenGesture()

    data class Navigate(val page: MainScreenPageData) : MainScreenGesture()
    data class PageGesture(val page: MainScreenPageData, val gesture: Any) : MainScreenGesture()
}
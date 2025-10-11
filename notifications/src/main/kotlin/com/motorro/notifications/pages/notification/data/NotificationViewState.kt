package com.motorro.notifications.pages.notification.data

sealed class NotificationViewState {
    data object Loading : NotificationViewState()
    data class Form(val data: NotificationData, val sendEnabled: Boolean) : NotificationViewState()
}

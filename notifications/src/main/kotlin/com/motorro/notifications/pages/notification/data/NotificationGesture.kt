package com.motorro.notifications.pages.notification.data

sealed class NotificationGesture {
    data class TitleChanged(val title: String) : NotificationGesture()
    data class TextChanged(val text: String) : NotificationGesture()
    data object Send : NotificationGesture()
}
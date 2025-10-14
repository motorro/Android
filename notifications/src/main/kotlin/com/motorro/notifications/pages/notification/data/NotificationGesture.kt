package com.motorro.notifications.pages.notification.data

import com.motorro.notifications.MyNotificationChannel

sealed class NotificationGesture {
    data class TitleChanged(val title: String) : NotificationGesture()
    data class TextChanged(val text: String) : NotificationGesture()
    data class ChannelChanged(val channel: MyNotificationChannel) : NotificationGesture()
    data object UpdateLatestToggled : NotificationGesture()
    data object Send : NotificationGesture()
    data object Dismiss : NotificationGesture()
}
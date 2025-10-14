package com.motorro.notifications.pages.notification.data

import androidx.compose.runtime.Immutable
import com.motorro.notifications.MyNotificationChannel

sealed class NotificationViewState {
    data object Loading : NotificationViewState()

    @Immutable
    data class Form(
        val data: NotificationData,
        val availableChannels: List<MyNotificationChannel>,
        val sendEnabled: Boolean
    ) : NotificationViewState()
}

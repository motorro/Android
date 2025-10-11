package com.motorro.notifications.pages.notification.state

import androidx.lifecycle.SavedStateHandle

interface NotificationContext {
    val factory: NotificationStateFactory
    val savedStateHandle: SavedStateHandle
}
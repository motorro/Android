package com.motorro.notifications.pages.notification.state

import android.content.Context
import androidx.lifecycle.SavedStateHandle

interface NotificationContext {
    val factory: NotificationStateFactory
    val savedStateHandle: SavedStateHandle
    val androidContext: Context
}
package com.motorro.notifications.pages.progress.state

import android.content.Context
import androidx.core.app.NotificationManagerCompat

interface ProgressContext {
    val factory: ProgressStateFactory
    val androidContext: Context
    val notificationManager: NotificationManagerCompat get() = NotificationManagerCompat.from(androidContext)
}
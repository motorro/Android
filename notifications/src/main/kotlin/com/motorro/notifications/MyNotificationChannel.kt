package com.motorro.notifications

import android.app.NotificationManager
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Notification channel definition
 */
enum class MyNotificationChannel(
    @field:StringRes val title: Int,
    @field:StringRes val description: Int,
    @field:DrawableRes val icon: Int,
    val importance: Int
) {
    URGENT(
        title = R.string.urgent_channel_title,
        description = R.string.urgent_channel_description,
        icon = R.drawable.ic_urgent,
        importance = NotificationManager.IMPORTANCE_HIGH
    ),
    HIGH(
        title = R.string.high_channel_title,
        description = R.string.high_channel_description,
        icon = R.drawable.ic_high,
        importance = NotificationManager.IMPORTANCE_DEFAULT
    ),
    MEDIUM(
        title = R.string.medium_channel_title,
        description = R.string.medium_channel_description,
        icon = R.drawable.ic_medium,
        importance = NotificationManager.IMPORTANCE_LOW
    ),
    LOW(
        title = R.string.low_channel_title,
        description = R.string.low_channel_description,
        icon = R.drawable.ic_low,
        importance = NotificationManager.IMPORTANCE_MIN
    )
}
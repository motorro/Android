package com.motorro.background

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
    ONGOING(
        title = R.string.ongoing_channel_title,
        description = R.string.ongoing_channel_description,
        icon = R.drawable.ic_low,
        importance = NotificationManager.IMPORTANCE_MIN
    )
}
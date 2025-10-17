package com.motorro.notifications

import android.content.Intent
import com.motorro.notifications.data.NotificationAction

private const val SCHEME = "app"
private const val EXTRA_NOTIFICATION_ID = "notificationId"

/**
 * Retrieves notification ID
 */
fun Intent.getNotificationId(): Int = requireNotNull(this.extras?.getInt(EXTRA_NOTIFICATION_ID)?.takeIf { it >= 0 }) {
    "Notification ID is not set"
}

/**
 * Checks if the intent comes from a notification
 */
fun Intent.isNotificationIntent(): Boolean = this.extras?.getInt(EXTRA_NOTIFICATION_ID) != null

/**
 * Checks if the intent comes from a notification
 */
fun Intent.asNotificationActionOrNull(): NotificationAction? = if (isNotificationIntent()) {
    NotificationAction(this)
} else {
    null
}
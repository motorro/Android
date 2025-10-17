package com.motorro.notifications.data

import android.content.Intent
import com.motorro.notifications.getNotificationId

/**
 * Notification action
 * @param intent Notification intent
 */
@JvmInline
value class NotificationAction(val intent: Intent) {
    val id: Int get() = intent.getNotificationId()
    val pathSegments: List<String> get() = intent.data?.pathSegments ?: emptyList()
}

package com.motorro.notifications

import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import androidx.core.app.PendingIntentCompat
import com.motorro.notifications.data.NotificationAction
import com.motorro.notifications.pages.notification.api.NotificationPageData
import com.motorro.notifications.pages.push.api.PushPageData
import com.motorro.notifications.pages.reply.api.ReplyPageData
import javax.inject.Inject

private const val SCHEME = "app"
private const val EXTRA_NOTIFICATION_ID = "notificationId"
private const val PUSH_NOTIFICATION_ID = 100500
private const val REQUEST_CODE = 100500


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

/**
 * Builds notification actions
 */
interface NotificationActionBuilder {
    /**
     * Opens the application
     */
    fun openApp(notificationId: Int): PendingIntent

    /**
     * Replies to the notification
     */
    fun reply(notificationId: Int): PendingIntent

    /**
     * Pushes notification
     */
    fun push(title: String, message: String, data: String?): Intent

    class Impl @Inject constructor(private val application: Application) : NotificationActionBuilder {
        override fun openApp(notificationId: Int): PendingIntent {
            val mainActivityIntent = createActivityIntent(notificationId, NotificationPageData.pathSegments)
            return requireNotNull(PendingIntentCompat.getActivity(
                /* context = */ application,
                /* requestCode = */ REQUEST_CODE,
                /* intent = */ mainActivityIntent,
                /* flags = */ PendingIntent.FLAG_ONE_SHOT,
                /* isMutable = */ false
            ))
        }

        override fun reply(notificationId: Int): PendingIntent {
            val replyIntent = createActivityIntent(notificationId, ReplyPageData.pathSegments)
            return requireNotNull(PendingIntentCompat.getActivity(
                /* context = */ application,
                /* requestCode = */ REQUEST_CODE,
                /* intent = */ replyIntent,
                /* flags = */ PendingIntent.FLAG_UPDATE_CURRENT,
                /* isMutable = */ true
            ))
        }

        override fun push(title: String, message: String, data: String?): Intent = createActivityIntent(PUSH_NOTIFICATION_ID, PushPageData.pathSegments).apply {
            putExtra(PushPageData.PUSH_TITLE_PARAM, title)
            putExtra(PushPageData.PUSH_MESSAGE_PARAM, message)
            putExtra(PushPageData.PUSH_DATA_PARAM, data)
        }

        private inline fun createActivityIntent(notificationId: Int, segments: List<String>, dataBuilder: Uri.Builder.() -> Unit = { }): Intent {
            return Intent(application, MainActivity::class.java).apply {
                // Put extra to get the notification later
                putExtra(EXTRA_NOTIFICATION_ID, notificationId)

                // Put data
                val data = Uri.Builder()
                    .scheme(SCHEME)
                    .apply { segments.forEach { appendPath(it) } }
                    .apply { dataBuilder() }
                    .build()

                setData(data)
            }
        }
    }
}
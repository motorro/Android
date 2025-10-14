package com.motorro.notifications.pages.notification.state

import android.app.Notification
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.motorro.notifications.R
import com.motorro.notifications.pages.notification.data.NotificationData

/**
 * Sends the notification
 */
class SendingNotification(
    context: NotificationContext,
    private val toSend: NotificationData
) : BaseNotificationState(context) {

    override fun doStart() {
        super.doStart()

        var id = getLatestId()
        if (null == id) {
            id = 1
        }
        send(id, buildNotification())
        setMachineState(factory.form())
    }

    /**
     * Example of getting current App notifications
     * Note: Requires API 23+ to work correctly
     */
    private fun getLatestId(): Int? {
        val manager = NotificationManagerCompat.from(androidContext)
        return manager.activeNotifications.maxByOrNull { it.postTime }?.id
    }

    /**
     * Builds notification
     */
    private fun buildNotification() =
        NotificationCompat.Builder(androidContext, toSend.channel.name)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(toSend.title)
            .setContentText(toSend.text)
            .setAutoCancel(true)
            .build()

    /**
     * Sends notification
     */
    private fun send(id: Int, notification: Notification) {
        d { "Sending notification with ID: $id..." }
        val manager = NotificationManagerCompat.from(androidContext)
        if (manager.areNotificationsEnabled()) {
            manager.notify(id, notification)
        } else {
            throw IllegalStateException("Notifications are disabled. Shouldn't happen.")
        }
    }
}
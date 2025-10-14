package com.motorro.notifications.pages.notification.state

import androidx.core.app.NotificationManagerCompat

/**
 * Dismisses the notification
 */
class DismissingLatestNotification(context: NotificationContext) : BaseNotificationState(context) {

    override fun doStart() {
        super.doStart()

        val toDismiss = getLatestId()
        if (null != toDismiss) {
            dismiss(toDismiss)
        }
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
     * Dismisses notification given the notification ID
     */
    private fun dismiss(id: Int) {
        d { "Dismissing notification: $id..." }
        val manager = NotificationManagerCompat.from(androidContext)
        manager.cancel(id)
    }
}
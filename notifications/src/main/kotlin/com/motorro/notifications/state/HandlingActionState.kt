package com.motorro.notifications.state

import android.app.Application
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import com.motorro.notifications.data.NotificationAction
import javax.inject.Inject

class HandlingActionState(
    context: MainScreenContext,
    private val action: NotificationAction,
    private val androidContext: Context
) : BaseMainScreenState(context) {
    override fun doStart() {
        super.doStart()
        cancelNotification()
        dispatchNotification()
    }

    private fun cancelNotification() {
        i { "Dismissing notification: ${action.id}" }
        val notificationManager = NotificationManagerCompat.from(androidContext)
        notificationManager.cancel(action.id)
    }

    private fun dispatchNotification() {
        i { "Dispatching notification: ${action.id}" }
        val pageForAction = requireNotNull(factory.pageForAction(action)) {
            "Page for action not found: $action"
        }
        setMachineState(pageForAction)
    }

    class Factory @Inject constructor(private val app: Application) {
        operator fun invoke(context: MainScreenContext, action: NotificationAction) = HandlingActionState(
            context,
            action,
            app.applicationContext
        )
    }
}
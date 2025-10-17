package com.motorro.notifications.state

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.motorro.notifications.data.MainScreenViewState
import javax.inject.Inject

class NotificationCheckState(
    context: MainScreenContext,
    private val androidContext: Context,
    private val startAction: Intent
) : BaseMainScreenState(context) {

    override fun doStart() {
        super.doStart()
        setUiState(MainScreenViewState.Loading)
        checkForPermission()
    }

    private fun checkForPermission() {
        d { "Checking notification permissions" }
        when {
            areNotificationsEnabled().not() -> {
                d { "Notification permissions granted but not enabled" }
                setMachineState(factory.askingForPermissions(startAction))
            }
            else -> {
                d { "Notification permissions granted and enabled" }
                setMachineState(factory.creatingNotificationChannels(startAction))
            }
        }
    }

    /**
     * Checks if notifications are enabled
     */
    private fun areNotificationsEnabled() = NotificationManagerCompat.from(androidContext).areNotificationsEnabled()

    class Factory @Inject constructor(private val app: Application) {
        operator fun invoke(context: MainScreenContext, intent: Intent) = NotificationCheckState(
            context,
            app.applicationContext,
            intent
        )
    }
}


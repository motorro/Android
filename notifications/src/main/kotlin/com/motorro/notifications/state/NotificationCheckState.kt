package com.motorro.notifications.state

import android.app.Application
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import com.motorro.notifications.data.MainScreenViewState
import javax.inject.Inject

class NotificationCheckState(context: MainScreenContext, private val androidContext: Context) : BaseMainScreenState(context) {

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
                setMachineState(factory.askingForPermissions())
            }
            else -> {
                d { "Notification permissions granted and enabled" }
                setMachineState(factory.content())
            }
        }
    }

    /**
     * Checks if notifications are enabled
     */
    private fun areNotificationsEnabled() = NotificationManagerCompat.from(androidContext).areNotificationsEnabled()

    class Factory @Inject constructor(private val app: Application) {
        operator fun invoke(context: MainScreenContext) = NotificationCheckState(context, app.applicationContext)
    }
}


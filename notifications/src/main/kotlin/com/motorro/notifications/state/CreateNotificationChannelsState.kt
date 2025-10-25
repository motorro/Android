package com.motorro.notifications.state

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import com.motorro.notifications.MyNotificationChannel
import com.motorro.notifications.data.MainScreenViewState
import javax.inject.Inject

class CreateNotificationChannelsState(
    context: MainScreenContext,
    private val androidContext: Context,
    private val intent: Intent
) : BaseMainScreenState(context) {

    override fun doStart() {
        super.doStart()
        setUiState(MainScreenViewState.Loading)
        createChannels()
        setMachineState(factory.gettingFcmToken(intent))
    }

    /**
     * Creates channels.
     * Safe to call on app start with the original channel ID
     */
    private fun createChannels() {
        // Get notification manager
        val notificationManager = androidContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        MyNotificationChannel.entries.forEach {
            // Creates and register channel
            val channel = NotificationChannel(
                it.name,
                androidContext.getString(it.title),
                it.importance
            ).apply {
                description = androidContext.getString(it.description)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    class Factory @Inject constructor(private val app: Application) {
        operator fun invoke(context: MainScreenContext, intent: Intent) = CreateNotificationChannelsState(
            context,
            app.applicationContext,
            intent
        )
    }
}
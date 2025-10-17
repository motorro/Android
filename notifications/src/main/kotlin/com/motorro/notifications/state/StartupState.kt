package com.motorro.notifications.state

import android.content.Intent
import com.motorro.notifications.asNotificationActionOrNull
import com.motorro.notifications.data.MainScreenViewState

class StartupState(context: MainScreenContext, private val intent: Intent) : BaseMainScreenState(context) {
    override fun doStart() {
        super.doStart()
        setUiState(MainScreenViewState.Loading)
        checkIntent()
    }

    private fun checkIntent() {
        val notificationAction = intent.asNotificationActionOrNull()
        if (null != notificationAction) {
            i { "Got notification intent: $intent" }
            setMachineState(factory.handlingAction(notificationAction))
        } else {
            i { "No notification intent" }
            setMachineState(factory.mainContent())
        }
    }
}
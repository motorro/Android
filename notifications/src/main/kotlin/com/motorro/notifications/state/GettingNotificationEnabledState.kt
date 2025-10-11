package com.motorro.notifications.state

import com.motorro.notifications.data.MainScreenGesture
import com.motorro.notifications.data.MainScreenViewState

class GettingNotificationEnabledState(context: MainScreenContext) : BaseMainScreenState(context) {
    override fun doStart() {
        super.doStart()
        setUiState(MainScreenViewState.NeedToEnableNotifications)
    }

    override fun doProcess(gesture: MainScreenGesture) {
        when(gesture) {
            MainScreenGesture.Back -> {
                d { "Back gesture. Terminating..." }
            }
            is MainScreenGesture.NotificationPermissionRequested -> {
                if (gesture.granted) {
                    d { "Notification permission granted" }
                    setMachineState(factory.permissionsCheck())
                }
            }
            MainScreenGesture.RecheckNotificationPermissions -> {
                d { "Rechecking notification permissions" }
                setMachineState(factory.permissionsCheck())
            }
            else -> w { "Gesture not handled: $gesture" }
        }
    }
}
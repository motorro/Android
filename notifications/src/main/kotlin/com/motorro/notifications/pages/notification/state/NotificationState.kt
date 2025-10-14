package com.motorro.notifications.pages.notification.state

import androidx.annotation.CallSuper
import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.coroutines.CoroutineState
import com.motorro.core.log.Logging
import com.motorro.notifications.pages.notification.data.NotificationGesture
import com.motorro.notifications.pages.notification.data.NotificationViewState

typealias NotificationState = CommonMachineState<NotificationGesture, NotificationViewState>

abstract class BaseNotificationState(context: NotificationContext) : CoroutineState<NotificationGesture, NotificationViewState>(), NotificationContext by context, Logging {
    @CallSuper
    override fun doStart() {
        d { "Starting state: ${javaClass.simpleName}" }
    }

    override fun doProcess(gesture: NotificationGesture) {
        w { "Gesture not handled: $gesture" }
    }

    @CallSuper
    override fun doClear() {
        super.doClear()
        d { "Clearing state: ${javaClass.simpleName}" }
    }
}
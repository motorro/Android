package com.motorro.notifications.pages.progress.state

import android.app.Notification
import androidx.annotation.CallSuper
import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.coroutines.CoroutineState
import com.motorro.core.log.Logging
import com.motorro.notifications.pages.progress.data.ProgressGesture
import com.motorro.notifications.pages.progress.data.ProgressViewState

typealias ProgressState = CommonMachineState<ProgressGesture, ProgressViewState>

private const val NOTIFICATION_TAG = "progress"
private const val NOTIFICATION_ID = 100500

abstract class BaseProgressState(context: ProgressContext) : CoroutineState<ProgressGesture, ProgressViewState>(), ProgressContext by context, Logging {
    @CallSuper
    override fun doStart() {
        d { "Starting state: ${javaClass.simpleName}" }
    }

    override fun doProcess(gesture: ProgressGesture) {
        w { "Gesture not handled: $gesture" }
    }

    @CallSuper
    override fun doClear() {
        super.doClear()
        d { "Clearing state: ${javaClass.simpleName}" }
    }

    protected fun notify(notification: Notification) {
        if (notificationManager.areNotificationsEnabled()) {
            notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notification)
        } else {
            throw IllegalStateException("Notifications are disabled. Shouldn't happen.")
        }
    }

    protected fun cancelNotification() {
        notificationManager.cancel(NOTIFICATION_TAG, NOTIFICATION_ID)
    }
}
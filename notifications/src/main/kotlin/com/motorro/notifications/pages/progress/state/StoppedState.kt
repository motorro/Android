package com.motorro.notifications.pages.progress.state

import com.motorro.notifications.pages.progress.data.ProgressGesture
import com.motorro.notifications.pages.progress.data.ProgressViewState

class StoppedState(context: ProgressContext) : BaseProgressState(context) {
    override fun doStart() {
        super.doStart()
        setUiState(ProgressViewState.Player.Idle)
        cancelNotification()
    }

    override fun doProcess(gesture: ProgressGesture) {
        when(gesture) {
            ProgressGesture.Play -> {
                d { "Starting progress..." }
                setMachineState(factory.playing(0))
            }
            else -> super.doProcess(gesture)
        }
    }
}
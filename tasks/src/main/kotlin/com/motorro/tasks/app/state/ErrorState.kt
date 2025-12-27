package com.motorro.tasks.app.state

import com.motorro.core.error.WithRetry
import com.motorro.tasks.app.data.AppGesture
import com.motorro.tasks.app.data.AppUiState

class ErrorState(
    context: AppContext,
    private val error: Throwable,
    private val onRetry: () -> AppState,
    private val onBack: () -> AppState
) : BaseAppState(context) {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        super.doStart()
        setUiState(AppUiState.Error(error))
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: AppGesture) {
        when(gesture) {
            AppGesture.Back -> {
                d { "Back. Returning..." }
                setMachineState(onBack())
            }
            AppGesture.Action -> {
                if (error is WithRetry) {
                    d { "Retrying..." }
                    setMachineState(onRetry())
                } else {
                    d { "No retry available" }
                    setMachineState(factory.terminated())
                }
            }
            else -> super.doProcess(gesture)
        }
    }
}
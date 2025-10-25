package com.motorro.notifications.state

import com.motorro.notifications.data.MainScreenGesture
import com.motorro.notifications.data.MainScreenViewState
import java.io.IOException

class ErrorState(
    context: MainScreenContext,
    private val error: Throwable,
    private val onBack: MainScreenStateFactory.() -> MainScreenState,
    private val onRetry: MainScreenStateFactory.() -> MainScreenState
) : BaseMainScreenState(context) {
    override fun doStart() {
        super.doStart()
        setUiState(MainScreenViewState.Error(error, error.isFatal()))
    }

    override fun doProcess(gesture: MainScreenGesture) {
        when (gesture) {
            is MainScreenGesture.Back -> {
                setMachineState(factory.onBack())
            }
            is MainScreenGesture.Action -> {
                setMachineState(if (error.isFatal()) factory.terminated() else factory.onRetry())
            }
            else -> super.doProcess(gesture)
        }
    }

    companion object {
        private fun Throwable.isFatal(): Boolean = when(this) {
            is IOException -> false
            else -> true
        }
    }
}
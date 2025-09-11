package com.motorro.statemachine.statemachine

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.coroutines.CoroutineState
import com.motorro.statemachine.statemachine.data.AppGesture
import com.motorro.statemachine.statemachine.data.AppUiState
import io.github.aakira.napier.Napier

/**
 * State-machine state bound to gesture and ui state
 */
typealias AppState = CommonMachineState<AppGesture, AppUiState>

/**
 * Base application state
 */
abstract class BaseAppState(protected val factory: AppStateFactory) : CoroutineState<AppGesture, AppUiState>() {
    protected open val tag: String get() = "StateMachine"
    protected open val stateTag: String get() = this::class.simpleName ?: "BaseAppState"

    override fun doProcess(gesture: AppGesture) {
        warn { "Unsupported gesture: $gesture" }
    }

    private inline fun createMessage(message: () -> String): String {
        return "State: $stateTag. Message: ${message()}"
    }

    protected fun info(message: () -> String) {
        Napier.i(tag = tag, message = createMessage(message))
    }

    protected fun warn(error: Throwable? = null, message: () -> String) {
        Napier.w(tag = tag, throwable = error, message = createMessage(message))
    }

    override fun doStart() {
        super.doStart()
        info { "Starting state..." }
    }

    override fun doClear() {
        super.doClear()
        info { "Clearing state..." }
    }
}
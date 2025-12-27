package com.motorro.tasks.app.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.coroutines.CoroutineState
import com.motorro.core.log.Logging
import com.motorro.tasks.app.data.AppGesture
import com.motorro.tasks.app.data.AppUiState

/**
 * Application state type
 */
typealias AppState = CommonMachineState<AppGesture, AppUiState>

/**
 *
 */
open class BaseAppState(context: AppContext) : CoroutineState<AppGesture, AppUiState>(), AppContext by context, Logging {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        d { "${javaClass.simpleName} started" }
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: AppGesture) {
        w { "Unsupported gesture: $gesture" }
    }
}
package com.motorro.tasks.app.state

import com.motorro.tasks.app.data.AppUiState

/**
 * Terminating app state
 */
class Terminated(context: AppContext) : BaseAppState(context) {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        super.doStart()
        setUiState(AppUiState.Terminated)
    }
}
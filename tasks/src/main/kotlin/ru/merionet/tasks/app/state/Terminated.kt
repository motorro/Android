package ru.merionet.tasks.app.state

import ru.merionet.tasks.app.data.AppUiState

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
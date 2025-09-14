package com.motorro.statemachine.statemachine

import com.motorro.statemachine.statemachine.content.state.ContentState
import com.motorro.statemachine.statemachine.data.AppUiState
import com.motorro.statemachine.statemachine.logout.state.LoggingOutState

/**
 * Application state factory
 */
interface AppStateFactory {
    /**
     * Application flow is complete and no updates to expect
     */
    fun terminated(): AppState

    /**
     * Content
     */
    fun content(): AppState

    /**
     * Authenticating...
     */
    fun authenticating(): AppState

    /**
     * Logout flow
     */
    fun loggingOut(): AppState

    companion object {
        /**
         * Factory implementation
         */
        val Instance: AppStateFactory = object : AppStateFactory {
            override fun terminated() = object : BaseAppState(this) {
                override fun doStart() {
                    info { "Application terminated" }
                    setUiState(AppUiState.Terminated)
                }
            }

            override fun content() = ContentState.Factory()(this)

            override fun authenticating() = LoginProxy(this)

            override fun loggingOut() = LoggingOutState.Factory()(this)
        }
    }
}
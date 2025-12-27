package com.motorro.tasks.app.state

import com.motorro.core.lce.LceState
import com.motorro.tasks.app.data.AppData
import com.motorro.tasks.app.data.AppUiState
import com.motorro.tasks.auth.SessionManager
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * Logging out
 */
class LoggingOutState(
    context: AppContext,
    private val data: AppData,
    private val sessionManager: SessionManager
) : BaseAppState(context) {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        super.doStart()
        logout()
    }

    /**
     * Logs user out
     */
    private fun logout() {
        sessionManager.logout()
            .onEach { state ->
                when(state) {
                    is LceState.Loading -> {
                        setUiState(AppUiState.Loading())
                    }
                    is LceState.Content -> {
                        d { "Logged out. Returning to login screen..." }
                        setMachineState(factory.loggingIn(userName = data.user.name))
                    }
                    is LceState.Error -> {
                        w(state.error) { "Logout error" }
                        setMachineState(factory.logoutError(data, state.error))
                    }
                }
            }
            .launchIn(stateScope)
    }

    /**
     * State factory to inject all the external dependencies and keep the main factory
     * clean
     */
    class Factory @Inject constructor(private val sessionManager: SessionManager) {
        operator fun invoke(context: AppContext, data: AppData): AppState = LoggingOutState(
            context,
            data,
            sessionManager
        )
    }
}
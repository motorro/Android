package com.motorro.statemachine.statemachine.logout.state

import android.statemachine.statemachine.composeapp.generated.resources.Res
import android.statemachine.statemachine.composeapp.generated.resources.logging_out
import com.motorro.statemachine.common.session.SessionManager
import com.motorro.statemachine.statemachine.AppState
import com.motorro.statemachine.statemachine.AppStateFactory
import com.motorro.statemachine.statemachine.BaseAppState
import com.motorro.statemachine.statemachine.data.AppUiState
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

/**
 * Handles login request
 * @param factory State factory
 * @param sessionManager Session manager
 */
class LoggingOutState(factory: AppStateFactory, private val sessionManager: SessionManager) : BaseAppState(factory) {

    /**
     * Called when the state is started
     */
    override fun doStart() {
        super.doStart()
        info { "Logging out..." }
        stateScope.launch {
            setUiState(AppUiState.Loading(getString(Res.string.logging_out)))
            sessionManager.logout()
            info { "Successfully logged-out" }
            info { "Returning to Content..." }
            setMachineState(factory.content())
        }
    }

    /**
     * State factory
     * Use to inject dependencies and leave the state itself clean
     */
    class Factory() {
        operator fun invoke(factory: AppStateFactory): AppState = LoggingOutState(
            factory,
            SessionManager.Instance
        )
    }
}
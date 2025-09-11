package com.motorro.statemachine.statemachine.content.state

import com.motorro.statemachine.common.session.SessionManager
import com.motorro.statemachine.common.session.data.Session
import com.motorro.statemachine.statemachine.AppState
import com.motorro.statemachine.statemachine.AppStateFactory
import com.motorro.statemachine.statemachine.BaseAppState
import com.motorro.statemachine.statemachine.data.AppGesture
import com.motorro.statemachine.statemachine.data.ContentGesture
import com.motorro.statemachine.statemachine.data.ContentUiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Manages content application state
 * @param factory State factory
 * @param sessionManager Session manager
 */
class ContentState(factory: AppStateFactory, private val sessionManager: SessionManager) : BaseAppState(factory) {

    /**
     * Called when the state is started
     */
    override fun doStart() {
        super.doStart()

        stateScope.launch {
            sessionManager.session.collectLatest { session ->
                when (session) {
                    is Session.Active -> {
                        info { "Active session of: ${session.user.username}" }
                        info { "Changing UI state to Content..." }
                        // Update UI state
                        setUiState(ContentUiState(session.user.username))
                    }
                    Session.NotLoggedIn -> {
                        info { "Not logged in. Redirecting to authentication..." }
                        // Create a new state and set it as the machine state
                        setMachineState(factory.loginForm())
                    }
                }
            }
        }
    }

    /**
     * Called to process the gesture
     */
    override fun doProcess(gesture: AppGesture) {
        when (gesture) {
            AppGesture.Back -> {
                info { "Back pressed. Terminating..." }
                setMachineState(factory.terminated())
            }
            is ContentGesture.Logout -> {
                info { "Logging out..." }
                setMachineState(factory.loggingOut())
            }
            else -> super.doProcess(gesture)
        }
    }

    /**
     * State factory
     * Use to inject dependencies and leave the state itself clean
     */
    class Factory() {
        operator fun invoke(factory: AppStateFactory): AppState = ContentState(
            factory,
            SessionManager.Instance
        )
    }
}
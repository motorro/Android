package ru.merionet.tasks.app.state

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.merionet.core.lce.LceState
import ru.merionet.tasks.app.data.AppData
import ru.merionet.tasks.app.data.AppGesture
import ru.merionet.tasks.app.data.AppUiState
import ru.merionet.tasks.auth.SessionManager
import ru.merionet.tasks.auth.data.Session
import ru.merionet.tasks.domain.data.User
import javax.inject.Inject

/**
 * Starting state for the main flow
 * Checks auth and if there is already - launches the main flow
 * Otherwise launches the login flow
 */
class CheckingAuthState(
    context: AppContext,
    private val sessionManager: SessionManager
) : BaseAppState(context) {

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        super.doStart()
        checkAuth()
    }

    /**
     * Checks authentication status and advances to appropriate state
     */
    private fun checkAuth() {
        sessionManager.session
            .onEach { state ->
                when(state) {
                    is LceState.Loading -> {
                        d { "Loading session data..." }
                        setUiState(AppUiState.Loading())
                    }
                    is LceState.Content -> {
                        when(val session = state.data) {
                            Session.NONE -> {
                                d { "No active session. Transferring to login screen..." }
                                setMachineState(factory.loggingIn())
                            }
                            is Session.Active -> {
                                d { "Have active user!"}
                                onAuthenticated(session)
                            }
                        }
                    }
                    is LceState.Error -> {
                        w (state.error) { "Error loading session!" }
                        setMachineState(factory.initError(state.error))
                    }
                }
            }
            .launchIn(stateScope)
    }

    private fun onAuthenticated(session: Session.Active) {
        d { "Creating app-data and transferring to task-list..." }
        val user = User(session.claims.username)
        setMachineState(factory.taskList(AppData(user = user)))
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: AppGesture) {
        when(gesture) {
            AppGesture.Back -> {
                d { "Back. Terminating application..." }
                setMachineState(factory.terminated())
            }
            else -> super.doProcess(gesture)
        }
    }

    /**
     * State factory to inject all the external dependencies and keep the main factory
     * clean
     */
    class Factory @Inject constructor(private val sessionManager: SessionManager) {
        operator fun invoke(context: AppContext) = CheckingAuthState(
            context,
            sessionManager
        )
    }
}
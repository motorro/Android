package ru.merionet.tasks.login.state

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.merionet.core.lce.LceState
import ru.merionet.tasks.auth.SessionManager
import ru.merionet.tasks.data.AuthRequest
import ru.merionet.tasks.data.UserName
import ru.merionet.tasks.login.data.LoginData
import ru.merionet.tasks.login.data.LoginGesture
import ru.merionet.tasks.login.data.LoginUiState
import javax.inject.Inject

/**
 * Runs login operation on server
 */
class LoggingInState(
    context: LoginContext,
    private val data: LoginData,
    private val sessionManager: SessionManager
) : BaseLoginState(context) {

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        super.doStart()
        login()
    }

    private fun login() {
        sessionManager.authenticate(AuthRequest(UserName(data.userName), data.password))
            .onEach {
                when(it) {
                    is LceState.Loading -> {
                        setUiState(LoginUiState.Loading)
                    }
                    is LceState.Content -> {
                        d { "Logged-in successfully" }
                        setMachineState(factory.authenticated(data, it.data.claims))
                    }
                    is LceState.Error -> {
                        w(it.error) { "Login error" }
                        setMachineState(factory.loginError(data, it.error))
                    }
                }
            }
            .launchIn(stateScope)
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: LoginGesture) {
        when(gesture) {
            LoginGesture.Back -> {
                d { "Back pressed. Returning to login form..." }
                setMachineState(factory.form(data))
            }
            else -> super.doProcess(gesture)
        }
    }

    /**
     * State factory to inject all the external dependencies and keep the main factory
     * clean
     */
    class Factory @Inject constructor(private val sessionManager: SessionManager) {
        operator fun invoke(context: LoginContext, data: LoginData) = LoggingInState(
            context,
            data,
            sessionManager
        )
    }
}
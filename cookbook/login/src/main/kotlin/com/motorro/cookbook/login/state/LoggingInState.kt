package com.motorro.cookbook.login.state

import com.motorro.cookbook.core.error.toCore
import com.motorro.cookbook.domain.session.SessionManager
import com.motorro.cookbook.login.data.LoginFlowData
import com.motorro.cookbook.login.data.LoginGesture
import com.motorro.cookbook.login.data.LoginViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Runs login on server
 */
internal class LoggingInState(
    context: LoginContext,
    private val data: LoginFlowData,
    private val sessionManager: SessionManager
) : LoginState(context) {

    override fun doStart() {
        super.doStart()
        setUiState(LoginViewState.Loading(data.username, data.password))
        login()
    }

    /**
     * Runs login on server
     */
    private fun login() = stateScope.launch {
        sessionManager.login(data.username, data.password)
            .onSuccess {
                d { "Login success" }
                setMachineState(factory.complete(it))
            }
            .onFailure {
                w(it) { "Login failed" }
                setMachineState(factory.form(data, it.toCore()))
            }
    }

    override fun doProcess(gesture: LoginGesture) {
        when (gesture) {
            LoginGesture.Back -> {
                d { "Back gesture. Returning to form..." }
                setMachineState(factory.form(data))
            }
            else -> super.doProcess(gesture)
        }
    }

    /**
     * Factory for [LoggingInState]
     */
    class Factory @Inject constructor(private val sessionManager: SessionManager) {
        operator fun invoke(context: LoginContext, data: LoginFlowData) = LoggingInState(
            context,
            data,
            sessionManager
        )
    }
}
package com.motorro.cookbook.loginsocial.state

import com.motorro.cookbook.core.error.toCore
import com.motorro.cookbook.domain.session.SessionManager
import com.motorro.cookbook.loginsocial.Credentials
import com.motorro.cookbook.loginsocial.data.LoginSocialGesture
import com.motorro.cookbook.loginsocial.data.LoginSocialProvider
import com.motorro.cookbook.loginsocial.data.LoginSocialViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Runs login on server
 */
internal class LoggingInSocialState(
    context: LoginSocialContext,
    private val providerId: String,
    private val sessionManager: SessionManager
) : LoginSocialState(context) {

    override fun doStart() {
        super.doStart()
        render()
        login()
    }

    /**
     * Runs login on server
     */
    private fun login() = stateScope.launch {
        sessionManager.login(Credentials.username, Credentials.password)
            .onSuccess {
                d { "Login success" }
                setMachineState(factory.complete(it))
            }
            .onFailure {
                w(it) { "Login failed" }
                setMachineState(factory.buttons(it.toCore()))
            }
    }

    override fun doProcess(gesture: LoginSocialGesture) {
        when (gesture) {
            LoginSocialGesture.Back -> {
                d { "Back gesture. Returning to form..." }
                setMachineState(factory.buttons())
            }
            else -> super.doProcess(gesture)
        }
    }

    private fun render() {
        val provider = LoginSocialProvider.valueOf(providerId)
        setUiState(LoginSocialViewState.LoggingIn(provider.providerName, provider.icon))
    }

    /**
     * Factory for [LoggingInSocialState]
     */
    class Factory @Inject constructor(private val sessionManager: SessionManager) {
        operator fun invoke(context: LoginSocialContext, providerId: String) = LoggingInSocialState(
            context,
            providerId,
            sessionManager
        )
    }
}
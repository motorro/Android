package com.motorro.tasks.app.state

import com.motorro.commonstatemachine.ProxyMachineState
import com.motorro.core.log.Logging
import com.motorro.tasks.app.data.AppGesture
import com.motorro.tasks.app.data.AppUiState
import com.motorro.tasks.domain.data.User
import com.motorro.tasks.login.LoginFlowHost
import com.motorro.tasks.login.data.LoginGesture
import com.motorro.tasks.login.data.LoginUiState
import com.motorro.tasks.login.state.LoginStateFactory
import javax.inject.Inject

/**
 * Runs login flow
 */
class LoginProxy(
    private val context: AppContext,
    private val userName: String?,
    private val message: String?,
    private val onAuthGranted: (User) -> AppState,
    private val loginFactory: LoginStateFactory
) : ProxyMachineState<AppGesture, AppUiState, LoginGesture, LoginUiState>(LoginUiState.Loading), LoginFlowHost, Logging {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        super.doStart()
        d { "${javaClass.simpleName} started" }
    }

    /**
     * Creates initial child state
     */
    override fun init() = loginFactory.init(this, userName, message)

    /**
     * Maps child UI state to parent
     * @param child Child UI state
     */
    override fun mapUiState(child: LoginUiState): AppUiState = AppUiState.LoggingIn(child)

    /**
     * Maps parent UI state to child if relevant
     * @param parent Parent gesture
     * @return Mapped gesture or null if not applicable
     */
    override fun mapGesture(parent: AppGesture): LoginGesture? = when(parent) {
        AppGesture.Back -> LoginGesture.Back
        AppGesture.Action -> LoginGesture.Action
        is AppGesture.Login -> parent.child
        else -> null
    }

    /**
     * When user authenticated
     * @param user Logged-in user
     */
    override fun onAuthenticated(user: User) {
        d { "User $userName authenticated. Running next state..." }
        setMachineState(onAuthGranted(user))
    }

    /**
     * When user failed or refused to authenticate
     */
    override fun onNotAuthenticated() {
        d { "User did not authenticate or cancelled. Terminating..." }
        setMachineState(context.factory.terminated())
    }

    /**
     * State factory to inject all the external dependencies and keep the main factory
     * clean
     */
    class Factory @Inject constructor(private val loginFactory: LoginStateFactory) {
        operator fun invoke(
            context: AppContext,
            userName: String?,
            message: String?,
            onAuthGranted: (User) -> AppState
        ): AppState = LoginProxy(
            context,
            userName,
            message,
            onAuthGranted,
            loginFactory
        )
    }
}
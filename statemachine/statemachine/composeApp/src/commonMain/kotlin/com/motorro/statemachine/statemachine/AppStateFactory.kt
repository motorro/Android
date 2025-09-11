package com.motorro.statemachine.statemachine

import com.motorro.statemachine.common.data.exception.AppException
import com.motorro.statemachine.statemachine.content.state.ContentState
import com.motorro.statemachine.statemachine.data.AppUiState
import com.motorro.statemachine.statemachine.login.data.LoginDataState
import com.motorro.statemachine.statemachine.login.state.LoggingInState
import com.motorro.statemachine.statemachine.login.state.LoginErrorState
import com.motorro.statemachine.statemachine.login.state.LoginFormState
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
     * Login form
     */
    fun loginForm(data: LoginDataState? = null): AppState

    /**
     * Running log-in
     */
    fun loggingIn(data: LoginDataState): AppState

    /**
     * Login error
     */
    fun loginError(data: LoginDataState, error: AppException): AppState

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

            override fun loginForm(data: LoginDataState?) = LoginFormState(
                this,
                data ?: LoginDataState()
            )

            override fun loggingIn(data: LoginDataState) = LoggingInState.Factory()(this, data)

            override fun loginError(data: LoginDataState, error: AppException) = LoginErrorState(
                this,
                data,
                error
            )

            override fun loggingOut() = LoggingOutState.Factory()(this)
        }
    }
}
package com.motorro.cookbook.loginsocial.state

import com.motorro.cookbook.core.error.CoreException
import com.motorro.cookbook.loginsocial.data.LoginSocialGesture
import com.motorro.cookbook.loginsocial.data.LoginSocialProvider
import com.motorro.cookbook.loginsocial.data.LoginSocialViewState
import kotlin.properties.Delegates

/**
 * Manages login form
 */
internal class LoginSocialButtonsState(
    context: LoginSocialContext,
    error: CoreException? = null
) : LoginSocialState(context) {

    private var error: CoreException? by Delegates.observable(error) { _, _, _ ->
        render()
    }

    override fun doStart() {
        super.doStart()
        render()
    }

    override fun doProcess(gesture: LoginSocialGesture) {
        when(gesture) {
            LoginSocialGesture.Back -> {
                d { "Back gesture. Terminating..." }
                setMachineState(factory.cancelled())
            }
            LoginSocialGesture.DismissError -> {
                d { "Dismiss error. Clearing error..." }
                error = null
            }
            is LoginSocialGesture.LoginSocial -> {
                d { "Valid data. Transferring to login..." }
                setMachineState(factory.loggingIn(gesture.providerId))
            }
        }
    }

    private fun render() {
        setUiState(
            LoginSocialViewState.Buttons(
                buttons = LoginSocialProvider.entries.map {
                    LoginSocialViewState.Buttons.Button(
                        id = it.name,
                        title = it.providerName,
                        icon = it.icon
                    )
                },
                error = error?.message
            )
        )
    }
}
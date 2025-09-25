package com.motorro.cookbook.login.state

import com.motorro.cookbook.login.data.LoginViewState
import com.motorro.cookbook.model.Profile

/**
 * End of flow
 */
internal class TerminatedState(context: LoginContext, private val profile: Profile? = null) : LoginState(context) {
    override fun doStart() {
        super.doStart()
        // At the moment we don't care about the result, so we just emit the final state
        setUiState(LoginViewState.LoggedIn)
    }
}
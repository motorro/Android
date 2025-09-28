package com.motorro.cookbook.login.state

import com.motorro.cookbook.model.Profile

/**
 * End of flow
 */
internal class TerminatedState(context: LoginContext, private val profile: Profile? = null) : LoginState(context) {
    override fun doStart() {
        super.doStart()
        if (null != profile) {
            flowHost.onLogin(profile)
        } else {
            flowHost.onCancel()
        }
    }
}
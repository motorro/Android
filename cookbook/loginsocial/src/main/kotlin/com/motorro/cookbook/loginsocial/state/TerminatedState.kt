package com.motorro.cookbook.loginsocial.state

import com.motorro.cookbook.model.Profile

/**
 * End of flow
 */
internal class TerminatedState(context: LoginSocialContext, private val profile: Profile? = null) : LoginSocialState(context) {
    override fun doStart() {
        super.doStart()
        if (null != profile) {
            flowHost.onLogin(profile)
        } else {
            flowHost.onCancel()
        }
    }
}
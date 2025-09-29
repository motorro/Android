package com.motorro.cookbook.loginsocial.state

import androidx.annotation.CallSuper
import com.motorro.commonstatemachine.coroutines.CoroutineState
import com.motorro.cookbook.core.log.Logging
import com.motorro.cookbook.loginsocial.data.LoginSocialGesture
import com.motorro.cookbook.loginsocial.data.LoginSocialViewState

/**
 * Base social login state
 */
internal abstract class LoginSocialState(context: LoginSocialContext) : CoroutineState<LoginSocialGesture, LoginSocialViewState>(), LoginSocialContext by context, Logging {

    @CallSuper
    override fun doStart() {
        d { "Starting..." }
    }

    override fun doProcess(gesture: LoginSocialGesture) {
        w { "Gesture $gesture is not supported" }
    }

    @CallSuper
    override fun doClear() {
        super.doClear()
        d { "Cleared" }
    }
}
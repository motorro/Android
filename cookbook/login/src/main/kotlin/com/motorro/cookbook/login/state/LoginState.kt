package com.motorro.cookbook.login.state

import androidx.annotation.CallSuper
import com.motorro.commonstatemachine.coroutines.CoroutineState
import com.motorro.cookbook.core.log.Logging
import com.motorro.cookbook.login.data.LoginGesture
import com.motorro.cookbook.login.data.LoginViewState

/**
 * Base login state
 */
internal abstract class LoginState(context: LoginContext) : CoroutineState<LoginGesture, LoginViewState>(), LoginContext by context, Logging {

    @CallSuper
    override fun doStart() {
        d { "Starting..." }
    }

    override fun doProcess(gesture: LoginGesture) {
        w { "Gesture $gesture is not supported" }
    }

    @CallSuper
    override fun doClear() {
        super.doClear()
        d { "Cleared" }
    }
}
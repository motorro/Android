package ru.merionet.tasks.login.state

import com.motorro.commonstatemachine.coroutines.CoroutineState
import ru.merionet.core.log.Logging
import ru.merionet.tasks.login.data.LoginGesture
import ru.merionet.tasks.login.data.LoginUiState

/**
 * Basic state class with fixed gesture and ui-state systems
 */
typealias LoginState = CoroutineState<LoginGesture, LoginUiState>

/**
 * Base class for login flow state
 * - Encapsulates context
 * - Has default gesture processing logic
 * - Tags logging
 */
abstract class BaseLoginState(context: LoginContext): LoginState(), LoginContext by context, Logging {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        d { "${javaClass.simpleName} started" }
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: LoginGesture) {
        w { "Unsupported gesture: $gesture" }
    }
}
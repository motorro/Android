package com.motorro.statemachine.login.state

import com.motorro.statemachine.common.data.exception.toAppException
import com.motorro.statemachine.common.session.SessionManager
import com.motorro.statemachine.login.Res
import com.motorro.statemachine.login.data.LoginDataState
import com.motorro.statemachine.login.data.LoginFlowGesture
import com.motorro.statemachine.login.data.LoginFlowUiState
import com.motorro.statemachine.login.logging_in
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

/**
 * Handles login request
 * @param context Login flow context
 * @param data Login data
 * @param sessionManager Session manager
 */
internal class LoggingInState(
    context: LoginContext,
    private val data: LoginDataState,
    private val sessionManager: SessionManager
) : BaseLoginState(context) {

    /**
     * Called when the state is started
     */
    override fun doStart() {
        super.doStart()
        info { "Logging in..." }
        stateScope.launch {
            setUiState(LoginFlowUiState.Loading(getString(Res.string.logging_in)))
            try {
                val session = sessionManager.login(data.username, data.password)
                info { "Successfully logged-in: ${session.user.username}" }
                info { "Finishing flow..." }
                flowHost.success(session.user.username)
            } catch (e: Throwable) {
                currentCoroutineContext().ensureActive()
                warn(e) { "Failed to login. Transferring to error..." }
                setMachineState(factory.loginError(data, e.toAppException()))
            }
        }
    }

    override fun doProcess(gesture: LoginFlowGesture) {
        when(gesture) {
            is LoginFlowGesture.Back -> {
                info { "Back pressed. Returning to form..." }
                setMachineState(factory.loginForm(data))
            }
            else -> super.doProcess(gesture)
        }
    }

    /**
     * State factory
     * Use to inject dependencies and leave the state itself clean
     */
    class Factory() {
        operator fun invoke(context: LoginContext, data: LoginDataState) = LoggingInState(
            context,
            data,
            SessionManager.Instance
        )
    }
}
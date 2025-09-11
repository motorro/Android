package com.motorro.statemachine.statemachine.login.state

import android.statemachine.statemachine.composeapp.generated.resources.Res
import android.statemachine.statemachine.composeapp.generated.resources.logging_in
import com.motorro.statemachine.common.data.exception.toAppException
import com.motorro.statemachine.common.session.SessionManager
import com.motorro.statemachine.statemachine.AppState
import com.motorro.statemachine.statemachine.AppStateFactory
import com.motorro.statemachine.statemachine.BaseAppState
import com.motorro.statemachine.statemachine.data.AppGesture
import com.motorro.statemachine.statemachine.data.AppUiState
import com.motorro.statemachine.statemachine.login.data.LoginDataState
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

/**
 * Handles login request
 * @param factory State factory
 * @param data Login data
 * @param sessionManager Session manager
 */
class LoggingInState(
    factory: AppStateFactory,
    private val data: LoginDataState,
    private val sessionManager: SessionManager
) : BaseAppState(factory) {

    /**
     * Called when the state is started
     */
    override fun doStart() {
        super.doStart()
        info { "Logging in..." }
        stateScope.launch {
            setUiState(AppUiState.Loading(getString(Res.string.logging_in)))
            try {
                val user = sessionManager.login(data.username, data.password)
                info { "Successfully logged-in: ${user.user.username}" }
                info { "Returning to Content..." }
                setMachineState(factory.content())
            } catch (e: Throwable) {
                currentCoroutineContext().ensureActive()
                warn(e) { "Failed to login. Transferring to error..." }
                setMachineState(factory.loginError(data, e.toAppException()))
            }
        }
    }

    override fun doProcess(gesture: AppGesture) {
        when(gesture) {
            is AppGesture.Back -> {
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
        operator fun invoke(factory: AppStateFactory, data: LoginDataState): AppState = LoggingInState(
            factory,
            data,
            SessionManager.Instance
        )
    }
}
package com.motorro.statemachine.register.state

import com.motorro.statemachine.common.data.exception.toAppException
import com.motorro.statemachine.common.session.SessionManager
import com.motorro.statemachine.common.session.data.User
import com.motorro.statemachine.register.Res
import com.motorro.statemachine.register.data.RegisterDataState
import com.motorro.statemachine.register.data.RegisterFlowGesture
import com.motorro.statemachine.register.data.RegisterFlowUiState
import com.motorro.statemachine.register.registering
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

/**
 * Handles registration request
 * @param context Register flow context
 * @param data Register data
 * @param sessionManager Session manager
 */
internal class RegisteringState(
    context: RegisterContext,
    private val data: RegisterDataState,
    private val sessionManager: SessionManager
) : BaseRegisterState(context) {

    /**
     * Called when the state is started
     */
    override fun doStart() {
        super.doStart()
        info { "Logging in..." }
        stateScope.launch {
            setUiState(RegisterFlowUiState.Loading(getString(Res.string.registering)))
            try {
                val session = sessionManager.register(
                    User(
                        data.username,
                        data.password,
                        data.email
                    )
                )
                info { "Successfully registered: ${session.user.username}" }
                info { "Finishing flow..." }
                flowHost.success(session.user.username)
            } catch (e: Throwable) {
                currentCoroutineContext().ensureActive()
                warn(e) { "Failed to login. Transferring to error..." }
                setMachineState(factory.registrationError(data, e.toAppException()))
            }
        }
    }

    override fun doProcess(gesture: RegisterFlowGesture) {
        when(gesture) {
            is RegisterFlowGesture.Back -> {
                info { "Back pressed. Returning to form..." }
                setMachineState(factory.eulaForm(data))
            }
            else -> super.doProcess(gesture)
        }
    }

    /**
     * State factory
     * Use to inject dependencies and leave the state itself clean
     */
    class Factory() {
        operator fun invoke(context: RegisterContext, data: RegisterDataState) = RegisteringState(
            context,
            data,
            SessionManager.Instance
        )
    }
}
package ru.merionet.tasks.login.state

import ru.merionet.tasks.login.data.LoginData
import ru.merionet.tasks.login.data.LoginGesture
import ru.merionet.tasks.login.data.LoginUiState

/**
 * Runs login operation on server
 */
class LoggingInState(context: LoginContext, private val data: LoginData) : BaseLoginState(context) {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        super.doStart()
        setUiState(LoginUiState.Loading)
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: LoginGesture) {
        when(gesture) {
            LoginGesture.Back -> {
                d { "Back pressed. Returning to login form..." }
                setMachineState(factory.form(data))
            }
            else -> super.doProcess(gesture)
        }
    }
}
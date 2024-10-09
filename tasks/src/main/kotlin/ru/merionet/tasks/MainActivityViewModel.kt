package ru.merionet.tasks

import androidx.lifecycle.ViewModel
import com.motorro.commonstatemachine.coroutines.FlowStateMachine
import kotlinx.coroutines.flow.StateFlow
import ru.merionet.tasks.login.data.LoginGesture
import ru.merionet.tasks.login.data.LoginUiState
import ru.merionet.tasks.login.state.LoginStateFactory

/**
 * Common view-model for the app
 */
class MainActivityViewModel : ViewModel() {
    /**
     * Common state machine
     */
    private val stateMachine = FlowStateMachine(LoginUiState.Loading) {
        LoginStateFactory.Impl().init()
    }

    /**
     * UI state export
     */
    val uiState: StateFlow<LoginUiState> get() = stateMachine.uiState

    /**
     * Gesture processing
     */
    fun process(gesture: LoginGesture) {
        stateMachine.process(gesture)
    }
}
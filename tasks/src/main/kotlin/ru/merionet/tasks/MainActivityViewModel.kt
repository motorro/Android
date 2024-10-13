package ru.merionet.tasks

import androidx.lifecycle.ViewModel
import com.motorro.commonstatemachine.coroutines.FlowStateMachine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import ru.merionet.tasks.login.data.LoginGesture
import ru.merionet.tasks.login.data.LoginUiState
import ru.merionet.tasks.login.state.LoginStateFactory
import javax.inject.Inject

/**
 * Common view-model for the app
 */
@HiltViewModel
class MainActivityViewModel @Inject constructor(loginStateFactory: LoginStateFactory) : ViewModel() {
    /**
     * Common state machine
     */
    private val stateMachine = FlowStateMachine(LoginUiState.Loading) {
        loginStateFactory.init()
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
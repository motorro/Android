package com.motorro.tasks

import androidx.lifecycle.ViewModel
import com.motorro.commonstatemachine.coroutines.FlowStateMachine
import com.motorro.tasks.login.data.LoginGesture
import com.motorro.tasks.login.data.LoginUiState
import com.motorro.tasks.login.state.LoginStateFactory
import kotlinx.coroutines.flow.StateFlow

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
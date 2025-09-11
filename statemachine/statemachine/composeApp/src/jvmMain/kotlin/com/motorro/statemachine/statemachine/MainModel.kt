package com.motorro.statemachine.statemachine

import com.motorro.commonstatemachine.coroutines.FlowStateMachine
import com.motorro.statemachine.statemachine.data.AppGesture
import com.motorro.statemachine.statemachine.data.AppUiState
import kotlinx.coroutines.flow.StateFlow

class MainModel {
    private val stateMachine = FlowStateMachine(AppUiState.Loading()) {
        AppStateFactory.Instance.content()
    }

    val uiState: StateFlow<AppUiState> get() = stateMachine.uiState

    fun process(gesture: AppGesture) {
        stateMachine.process(gesture)
    }
}
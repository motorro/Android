package com.motorro.tasks

import androidx.lifecycle.ViewModel
import com.motorro.commonstatemachine.coroutines.FlowStateMachine
import com.motorro.tasks.app.data.AppGesture
import com.motorro.tasks.app.data.AppUiState
import com.motorro.tasks.app.state.AppStateFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * Common view-model for the app
 */
@HiltViewModel
class MainActivityViewModel @Inject constructor(appStateFactory: AppStateFactory) : ViewModel() {
    /**
     * Common state machine
     */
    private val stateMachine = FlowStateMachine(AppUiState.Loading()) {
        appStateFactory.init()
    }

    /**
     * UI state export
     */
    val uiState: StateFlow<AppUiState> get() = stateMachine.uiState

    /**
     * Gesture processing
     */
    fun process(gesture: AppGesture) {
        stateMachine.process(gesture)
    }
}
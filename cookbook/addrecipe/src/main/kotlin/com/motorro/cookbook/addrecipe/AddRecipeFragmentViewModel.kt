package com.motorro.cookbook.addrecipe

import androidx.lifecycle.ViewModel
import com.motorro.commonstatemachine.coroutines.FlowStateMachine
import com.motorro.cookbook.addrecipe.data.AddRecipeGesture
import com.motorro.cookbook.addrecipe.data.AddRecipeViewState
import com.motorro.cookbook.addrecipe.state.AddRecipeStateFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AddRecipeFragmentViewModel @Inject internal constructor(stateFactory: AddRecipeStateFactory) : ViewModel() {
    private val stateMachine = FlowStateMachine(AddRecipeViewState.EMPTY) {
        stateFactory.init()
    }

    /**
     * View state
     */
    val viewState: StateFlow<AddRecipeViewState> = stateMachine.uiState

    /**
     * Gesture processing
     */
    fun process(gesture: AddRecipeGesture) {
        stateMachine.process(gesture)
    }
}
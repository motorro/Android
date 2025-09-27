package com.motorro.cookbook.recipelist

import androidx.lifecycle.ViewModel
import com.motorro.commonstatemachine.coroutines.FlowStateMachine
import com.motorro.cookbook.recipelist.data.RecipeListGesture
import com.motorro.cookbook.recipelist.data.RecipeListViewState
import com.motorro.cookbook.recipelist.state.RecipeListStateFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject internal constructor(stateFactory: RecipeListStateFactory) : ViewModel() {
    private val stateMachine = FlowStateMachine(RecipeListViewState.Loading) {
        stateFactory.init()
    }

    /**
     * View state
     */
    val viewState: StateFlow<RecipeListViewState> = stateMachine.uiState

    /**
     * Gesture processing
     */
    fun process(gesture: RecipeListGesture) {
        stateMachine.process(gesture)
    }
}
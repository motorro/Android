package com.motorro.cookbook.app

import androidx.lifecycle.ViewModel
import com.motorro.commonstatemachine.coroutines.FlowStateMachine
import com.motorro.cookbook.app.data.CookbookGesture
import com.motorro.cookbook.app.data.CookbookViewState
import com.motorro.cookbook.app.state.CookbookStateFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(factory: CookbookStateFactory) : ViewModel() {
    private val stateMachine = FlowStateMachine(CookbookViewState.Loading) {
        factory.init()
    }

    /**
     * View state
     */
    val viewState: StateFlow<CookbookViewState> = stateMachine.uiState

    /**
     * Gesture processing
     */
    fun process(gesture: CookbookGesture) {
        stateMachine.process(gesture)
    }
}
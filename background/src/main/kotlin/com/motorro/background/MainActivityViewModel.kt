package com.motorro.background

import androidx.lifecycle.ViewModel
import com.motorro.background.data.MainScreenGesture
import com.motorro.background.data.MainScreenViewState
import com.motorro.background.state.MainScreenStateFactory
import com.motorro.commonstatemachine.coroutines.FlowStateMachine
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(factory: MainScreenStateFactory) : ViewModel() {
    val stateMachine = FlowStateMachine(MainScreenViewState.Loading) {
        factory.init()
    }

    val uiState get() = stateMachine.uiState
    fun process(gesture: MainScreenGesture) = stateMachine.process(gesture)
}
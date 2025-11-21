package com.motorro.release

import androidx.lifecycle.ViewModel
import com.motorro.commonstatemachine.coroutines.FlowStateMachine
import com.motorro.release.data.MainScreenGesture
import com.motorro.release.data.MainScreenViewState
import com.motorro.release.state.MainScreenStateFactory
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
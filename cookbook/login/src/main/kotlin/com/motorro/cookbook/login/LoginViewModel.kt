package com.motorro.cookbook.login

import androidx.lifecycle.ViewModel
import com.motorro.commonstatemachine.coroutines.FlowStateMachine
import com.motorro.cookbook.login.data.LoginFlowData
import com.motorro.cookbook.login.data.LoginGesture
import com.motorro.cookbook.login.data.LoginViewState
import com.motorro.cookbook.login.state.LoginStateFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModel @Inject internal constructor(stateFactory: LoginStateFactory) : ViewModel() {
    private val stateMachine = FlowStateMachine(LoginViewState.EMPTY) {
        stateFactory.form(LoginFlowData())
    }

    val viewState: StateFlow<LoginViewState> = stateMachine.uiState

    fun process(gesture: LoginGesture) {
        stateMachine.process(gesture)
    }
}
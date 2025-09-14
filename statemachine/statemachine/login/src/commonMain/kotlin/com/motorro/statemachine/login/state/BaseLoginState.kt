package com.motorro.statemachine.login.state

import com.motorro.statemachine.common.state.BaseCoroutineState
import com.motorro.statemachine.login.data.LoginFlowGesture
import com.motorro.statemachine.login.data.LoginFlowUiState

/**
 * Base login flow state
 */
internal abstract class BaseLoginState(context: LoginContext) : BaseCoroutineState<LoginFlowGesture, LoginFlowUiState>(), LoginContext by context
package com.motorro.statemachine.register.state

import com.motorro.statemachine.common.state.BaseCoroutineState
import com.motorro.statemachine.register.data.RegisterFlowGesture
import com.motorro.statemachine.register.data.RegisterFlowUiState

/**
 * Base login flow state
 */
internal abstract class BaseRegisterState(context: RegisterContext) : BaseCoroutineState<RegisterFlowGesture, RegisterFlowUiState>(), RegisterContext by context
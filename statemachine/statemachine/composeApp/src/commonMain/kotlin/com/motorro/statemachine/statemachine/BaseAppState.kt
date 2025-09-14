package com.motorro.statemachine.statemachine

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.statemachine.common.state.BaseCoroutineState
import com.motorro.statemachine.statemachine.data.AppGesture
import com.motorro.statemachine.statemachine.data.AppUiState

/**
 * State-machine state bound to gesture and ui state
 */
typealias AppState = CommonMachineState<AppGesture, AppUiState>

/**
 * Base application state
 */
abstract class BaseAppState(protected val factory: AppStateFactory) : BaseCoroutineState<AppGesture, AppUiState>()
package com.motorro.cookbook.recipedemo.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.cookbook.recipedemo.data.RecipeDemoGesture
import com.motorro.cookbook.recipedemo.data.RecipeDemoViewState

/**
 * Base state type
 */
typealias RecipeDemoState = CommonMachineState<RecipeDemoGesture, RecipeDemoViewState>
package com.motorro.cookbook.app.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.ProxyMachineState
import com.motorro.cookbook.app.data.CookbookGesture
import com.motorro.cookbook.app.data.CookbookViewState

/**
 * Application state
 */
typealias CookbookState = CommonMachineState<CookbookGesture, CookbookViewState>

/**
 * Application proxy
 */
typealias CookbookProxy<CG, CU> = ProxyMachineState<CookbookGesture, CookbookViewState, CG, CU>
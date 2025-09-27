package com.motorro.cookbook.addrecipe.api

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.cookbook.addrecipe.data.AddRecipeGesture
import com.motorro.cookbook.addrecipe.data.AddRecipeViewState
import com.motorro.cookbook.appcore.navigation.CommonFlowHost

/**
 * Module API
 */
interface AddRecipeApi {
    /**
     * Starts add-recipe flow
     */
    fun start(flowHost: CommonFlowHost): CommonMachineState<AddRecipeGesture, AddRecipeViewState>

    companion object Companion {
        /**
         * Default UI state
         */
        val DEFAULT_UI_STATE = AddRecipeViewState.EMPTY
    }
}

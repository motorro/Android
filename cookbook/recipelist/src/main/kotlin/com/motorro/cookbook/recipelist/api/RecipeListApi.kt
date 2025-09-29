package com.motorro.cookbook.recipelist.api

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.cookbook.appcore.navigation.CommonFlowHost
import com.motorro.cookbook.recipelist.data.RecipeListGesture
import com.motorro.cookbook.recipelist.data.RecipeListViewState

/**
 * Module API
 */
interface RecipeListApi {
    /**
     * Starts add-recipe flow
     * @param flowHost Flow host
     * @return Recipe-list flow state
     */
    fun start(flowHost: CommonFlowHost): CommonMachineState<RecipeListGesture, RecipeListViewState>

    companion object Companion {
        /**
         * Default UI state
         */
        val DEFAULT_UI_STATE = RecipeListViewState.EMPTY
    }
}

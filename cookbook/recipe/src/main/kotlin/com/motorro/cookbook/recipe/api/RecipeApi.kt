package com.motorro.cookbook.recipe.api

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.cookbook.appcore.navigation.CommonFlowHost
import com.motorro.cookbook.model.ListRecipe
import com.motorro.cookbook.recipe.data.RecipeGesture
import com.motorro.cookbook.recipe.data.RecipeViewState
import kotlin.uuid.Uuid

/**
 * Module API
 */
interface RecipeApi {
    /**
     * Starts add-recipe flow
     * @param recipeId Recipe ID
     * @param flowHost Flow host
     * @return Recipe flow state
     */
    fun start(recipeId: Uuid, flowHost: CommonFlowHost): CommonMachineState<RecipeGesture, RecipeViewState>

    /**
     * Starts add-recipe flow
     * @param flowHost Flow host
     * @param listRecipe Preloaded list recipe
     * @return Recipe flow state
     */
    fun start(listRecipe: ListRecipe, flowHost: CommonFlowHost): CommonMachineState<RecipeGesture, RecipeViewState>

    companion object Companion {
        /**
         * Default UI state
         */
        val DEFAULT_UI_STATE = RecipeViewState.EMPTY
    }
}

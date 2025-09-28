package com.motorro.cookbook.recipedemo.state

import com.motorro.commonstatemachine.ProxyMachineState
import com.motorro.cookbook.appcore.navigation.CommonFlowHost
import com.motorro.cookbook.model.ListRecipe
import com.motorro.cookbook.recipe.api.RecipeApi
import com.motorro.cookbook.recipe.data.RecipeGesture
import com.motorro.cookbook.recipe.data.RecipeViewState
import com.motorro.cookbook.recipedemo.data.RecipeDemoGesture
import com.motorro.cookbook.recipedemo.data.RecipeDemoViewState
import kotlin.uuid.Uuid

/**
 * Recipe proxy type (for the sake of readability)
 */
private typealias RecipeProxyType = ProxyMachineState<
        RecipeDemoGesture,
        RecipeDemoViewState,
        RecipeGesture,
        RecipeViewState>

/**
 * Recipe flow proxy
 */
class RecipeProxy(
    private val api: RecipeApi,
    private val recipeId: Uuid? = null,
    private val listRecipe: ListRecipe? = null
) : RecipeProxyType(RecipeApi.DEFAULT_UI_STATE) {

    private val flowHost = CommonFlowHost {
        setMachineState(StarterState(api))
    }

    override fun init() = when {
        recipeId != null -> api.start(recipeId, flowHost)
        listRecipe != null -> api.start(listRecipe, flowHost)
        else -> throw IllegalArgumentException("No recipeId or listRecipe provided")
    }

    override fun mapGesture(parent: RecipeDemoGesture): RecipeGesture? = when (parent) {
        is RecipeDemoGesture.Back -> RecipeGesture.Back
        is RecipeDemoGesture.RecipeFlow -> parent.child
        else -> null
    }

    override fun mapUiState(child: RecipeViewState) = RecipeDemoViewState.RecipeFlow(child)
}
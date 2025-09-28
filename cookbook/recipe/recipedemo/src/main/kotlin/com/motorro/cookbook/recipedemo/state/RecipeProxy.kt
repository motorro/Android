package com.motorro.cookbook.recipedemo.state

import com.motorro.commonstatemachine.ProxyMachineState
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
    private val recipeId: Uuid
) : RecipeProxyType(RecipeApi.DEFAULT_UI_STATE) {

    override fun init() = api.start(recipeId) {
        setMachineState(StarterState(api))
    }

    override fun mapGesture(parent: RecipeDemoGesture): RecipeGesture? = when (parent) {
        is RecipeDemoGesture.Back -> RecipeGesture.Back
        is RecipeDemoGesture.RecipeFlow -> parent.child
        else -> null
    }

    override fun mapUiState(child: RecipeViewState) = RecipeDemoViewState.RecipeFlow(child)
}
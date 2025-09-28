package com.motorro.cookbook.recipedemo.state

import com.motorro.cookbook.data.recipes.RECIPES
import com.motorro.cookbook.recipe.api.RecipeApi
import com.motorro.cookbook.recipedemo.data.RecipeDemoGesture
import com.motorro.cookbook.recipedemo.data.RecipeDemoViewState

/**
 * Starter state
 */
class StarterState(private val api: RecipeApi) : RecipeDemoState() {
    override fun doStart() {
        setUiState(RecipeDemoViewState.Starter)
    }

    override fun doProcess(gesture: RecipeDemoGesture) {
        when (gesture) {
            RecipeDemoGesture.Back -> {
                setUiState(RecipeDemoViewState.Terminated)
            }
            is RecipeDemoGesture.ToRecipe -> {
                setMachineState(RecipeProxy(api, RECIPES.first().id))
            }
            else -> super.doProcess(gesture)
        }
    }
}
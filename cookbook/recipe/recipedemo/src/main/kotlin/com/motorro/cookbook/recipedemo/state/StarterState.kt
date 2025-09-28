package com.motorro.cookbook.recipedemo.state

import com.motorro.cookbook.data.recipes.RECIPES
import com.motorro.cookbook.model.ListRecipe
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
                setMachineState(RecipeProxy(api, recipeId = RECIPES.first().id))
            }
            is RecipeDemoGesture.ToPreloadedRecipe -> {
                setMachineState(RecipeProxy(api, listRecipe = RECIPES.first().run {
                    ListRecipe(id, title, category, image, dateTimeCreated, deleted)
                }))
            }
            else -> super.doProcess(gesture)
        }
    }
}
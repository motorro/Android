package com.motorro.cookbook.recipe.state

import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.domain.recipes.RecipeRepository
import com.motorro.cookbook.recipe.data.RecipeFlowData
import com.motorro.cookbook.recipe.data.RecipeViewState
import javax.inject.Inject

/**
 * Deletes recipe
 */
internal class DeletingState(
    context: RecipeContext,
    private val data: RecipeFlowData,
    private val repository: RecipeRepository
) : RecipeState(context) {

    override fun doStart() {
        super.doStart()
        setUiState(RecipeViewState.Content(LceState.Loading(data.data.data), false))
        delete()
    }

    /**
     * Deletes recipe
     */
    private fun delete() {
        repository.deleteRecipe(data.id)
        d { "Deleted..." }
        setMachineState(factory.terminated())
    }

    /**
     * Factory for [DeletingState]
     */
    class Factory @Inject constructor(private val repository: RecipeRepository) {
        operator fun invoke(context: RecipeContext, data: RecipeFlowData) = DeletingState(
            context,
            data,
            repository
        )
    }
}
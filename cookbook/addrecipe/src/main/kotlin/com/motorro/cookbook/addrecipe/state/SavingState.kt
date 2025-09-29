package com.motorro.cookbook.addrecipe.state

import com.motorro.cookbook.addrecipe.data.renderForm
import com.motorro.cookbook.domain.recipes.RecipeRepository
import com.motorro.cookbook.domain.recipes.data.NewRecipe
import javax.inject.Inject

/**
 * Saves recipe
 */
internal class SavingState(
    context: AddRecipeContext,
    private val toSave: NewRecipe,
    private val repository: RecipeRepository
) : AddRecipeState(context) {

    override fun doStart() {
        super.doStart()
        setUiState(toSave.renderForm(emptyList(), true))
        save()
    }

    /**
     * Saves recipe
     */
    private fun save() {
        repository.addRecipe(toSave)
        d { "Saved..." }
        setMachineState(factory.terminated())
    }

    /**
     * Factory for [SavingState]
     */
    class Factory @Inject constructor(private val repository: RecipeRepository) {
        operator fun invoke(context: AddRecipeContext, toSave: NewRecipe) = SavingState(
            context,
            toSave,
            repository
        )
    }
}
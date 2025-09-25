package com.motorro.cookbook.recipe.state

import com.motorro.cookbook.recipe.data.RecipeFlowData
import com.motorro.cookbook.recipe.data.RecipeGesture
import com.motorro.cookbook.recipe.data.RecipeViewState

/**
 * Requests confirmation to delete
 */
internal class DeleteConfirmationState(
    context: RecipeContext,
    private val data: RecipeFlowData
) : RecipeState(context) {

    val recipe get() = requireNotNull(data.data.data) {
        "Recipe is not loaded"
    }

    override fun doStart() {
        super.doStart()
        setUiState(RecipeViewState.DeleteConfirmation(recipe))
    }

    override fun doProcess(gesture: RecipeGesture) {
        when(gesture) {
            RecipeGesture.Back, RecipeGesture.CancelDelete -> {
                d { "Deletion is not confirmed. Returning to recipe..." }
                setMachineState(factory.content(data))
            }
            RecipeGesture.ConfirmDelete -> {
                d { "Deletion is confirmed. Deleting recipe..." }
                setMachineState(factory.deleting(data))
            }
            else -> super.doProcess(gesture)
        }
    }
}
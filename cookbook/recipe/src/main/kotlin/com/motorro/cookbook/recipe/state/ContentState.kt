package com.motorro.cookbook.recipe.state

import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.core.lce.replaceEmptyData
import com.motorro.cookbook.domain.recipes.RecipeRepository
import com.motorro.cookbook.domain.recipes.data.RecipeLce
import com.motorro.cookbook.domain.session.error.UnauthorizedException
import com.motorro.cookbook.recipe.data.RecipeFlowData
import com.motorro.cookbook.recipe.data.RecipeGesture
import com.motorro.cookbook.recipe.data.RecipeViewState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.properties.Delegates

/**
 * Recipe content
 */
internal class ContentState(
    context: RecipeContext,
    data: RecipeFlowData,
    private val repository: RecipeRepository
) : RecipeState(context) {

    private var data: RecipeFlowData by Delegates.observable(data) { _, _, new ->
        render(new.data)
    }

    override fun doStart() {
        super.doStart()
        render(data.data)
        load()
    }

    /**
     * Example:
     * Recipe should exist to be deleted
     */
    private fun canDelete(): Boolean = null != data.data.data

    /**
     * Loads recipe
     */
    private fun load() {
        repository.getRecipe(data.id)
            .map { it.replaceEmptyData(data.data.data) }
            .onEach { data = data.copy(data = it) }
            .onEach {
                if (it is LceState.Error && it.error is UnauthorizedException) {
                    d { "Unauthorized. Navigating to authentication..." }
                    setMachineState(factory.authenticating(data.id))
                }
            }
            .launchIn(stateScope)
    }

    override fun doProcess(gesture: RecipeGesture) {
        when(gesture) {
            RecipeGesture.Back -> {
                d { "Back is pressed. Terminating..." }
                setMachineState(factory.terminated())
            }
            RecipeGesture.Delete -> {
                d { "Delete is pressed. Asking confirmation..." }
                setMachineState(factory.deleteConfirmation(data))
            }
            RecipeGesture.DismissError -> {
                val error = data.data as? LceState.Error ?: return
                if (error.error.isFatal) {
                    d { "Fatal error is pressed. Terminating..." }
                    setMachineState(factory.terminated())
                } else {
                    d { "Non-fatal error is pressed. Reloading recipe..." }
                    repository.synchronizeRecipe(data.id)
                }
            }
            else -> super.doProcess(gesture)
        }
    }

    /**
     * Renders recipe
     */
    fun render(data: RecipeLce) {
        setUiState(RecipeViewState.Content(data, canDelete()))
    }

    /**
     * Factory for [ContentState]
     */
    class Factory @Inject constructor(private val repository: RecipeRepository) {
        operator fun invoke(context: RecipeContext, data: RecipeFlowData) = ContentState(
            context,
            data,
            repository
        )
    }
}
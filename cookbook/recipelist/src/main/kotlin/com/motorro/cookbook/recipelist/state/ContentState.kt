package com.motorro.cookbook.recipelist.state

import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.domain.recipes.RecipeRepository
import com.motorro.cookbook.recipelist.data.RecipeListGesture
import com.motorro.cookbook.recipelist.data.RecipeListItemLce
import com.motorro.cookbook.recipelist.data.RecipeListViewState
import com.motorro.cookbook.recipelist.data.getRecipeList
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * Recipe-list content
 */
internal class ContentState(
    context: RecipeListContext,
    private val repository: RecipeRepository
) : RecipeListState(context) {

    override fun doStart() {
        super.doStart()
        setUiState(RecipeListViewState.Loading)
        load()
    }

    /**
     * Loads recipe
     */
    private fun load() {
        repository.getRecipeList().onEach(::render).launchIn(stateScope)
    }

    override fun doProcess(gesture: RecipeListGesture) {
        when(gesture) {
            RecipeListGesture.Back -> {
                d { "Back is pressed. Terminating..." }
                setMachineState(factory.terminated())
            }
            RecipeListGesture.Logout -> {
                d { "Logout pressed. Logging out..." }
                setMachineState(factory.loggingOut())
            }
            RecipeListGesture.Refresh -> {
                d { "Refresh is pressed. Reloading recipe-list..." }
                repository.synchronizeList()
            }
            RecipeListGesture.AddRecipeClicked -> {
                d { "Add recipe is pressed. Navigating to add-recipe..." }
                setMachineState(factory.addingRecipe())
            }
            RecipeListGesture.DismissError -> {
                d { "Dismiss error is pressed. Reloading recipe-list..." }
                repository.synchronizeList()
            }
            else -> super.doProcess(gesture)
        }
    }

    /**
     * Renders recipe
     */
    fun render(state: RecipeListItemLce) {
        setUiState(
            RecipeListViewState.Content(
                state = state,
                addEnabled = null != state.data,
                refreshEnabled = state !is LceState.Loading
            )
        )
    }

    /**
     * Factory for [ContentState]
     */
    class Factory @Inject constructor(private val repository: RecipeRepository) {
        operator fun invoke(context: RecipeListContext) = ContentState(
            context,
            repository
        )
    }
}
package com.motorro.cookbook.recipelist.state

import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.core.lce.map
import com.motorro.cookbook.core.lce.replaceEmptyData
import com.motorro.cookbook.domain.recipes.RecipeRepository
import com.motorro.cookbook.recipelist.data.RecipeListFlowData
import com.motorro.cookbook.recipelist.data.RecipeListGesture
import com.motorro.cookbook.recipelist.data.RecipeListViewState
import com.motorro.cookbook.recipelist.data.toRecipeListItems
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.properties.Delegates

/**
 * Recipe-list content
 */
internal class ContentState(
    context: RecipeListContext,
    data: RecipeListFlowData,
    private val repository: RecipeRepository
) : RecipeListState(context) {

    private var data: RecipeListFlowData by Delegates.observable(data) { _, _, new ->
        render(new)
    }

    override fun doStart() {
        super.doStart()
        render(data)
        load()
    }

    /**
     * Loads recipe
     */
    private fun load() {
        repository
            .recipes
            .map { it.replaceEmptyData(data.list.data) }
            .onEach { data = data.copy(list = it) }
            .launchIn(stateScope)
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
                setMachineState(factory.addingRecipe(data))
            }
            is RecipeListGesture.RecipeClicked -> {
                d { "Recipe is clicked. Navigating to recipe ${gesture.id}..." }
                val recipe = data.list.data?.firstOrNull { it.id == gesture.id }
                if (recipe != null) {
                    d { "Recipe is available. Navigating to recipe with preloaded DATA" }
                    setMachineState(factory.recipe(data, recipe))
                } else {
                    d { "Recipe is not available. Navigating to recipe with ID" }
                    setMachineState(factory.recipe(data, gesture.id))
                }
            }
            RecipeListGesture.DismissError -> {
                val state = data.list
                if (state is LceState.Error) {
                    if (state.error.isFatal) {
                        d { "Fatal error. Restarting..." }
                        setMachineState(factory.init())
                    } else {
                        d { "Non-fatal error. Reloading recipe-list..." }
                        repository.synchronizeList()
                    }
                }
            }
            else -> super.doProcess(gesture)
        }
    }

    /**
     * Renders recipe
     */
    fun render(state: RecipeListFlowData) {
        setUiState(
            RecipeListViewState.Content(
                state = state.list.map { it.toRecipeListItems() },
                addEnabled = null != state.list.data,
                refreshEnabled = state.list !is LceState.Loading
            )
        )
    }

    /**
     * Factory for [ContentState]
     */
    class Factory @Inject constructor(private val repository: RecipeRepository) {
        operator fun invoke(context: RecipeListContext, data: RecipeListFlowData) = ContentState(
            context,
            data,
            repository
        )
    }
}
package com.motorro.cookbook.recipelist.state

import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.domain.recipes.RecipeRepository
import com.motorro.cookbook.domain.recipes.data.RecipeListLce
import com.motorro.cookbook.recipelist.data.RecipeListGesture
import com.motorro.cookbook.recipelist.data.RecipeListViewState
import com.motorro.cookbook.recipelist.data.toRecipeListItems
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Test

internal class ContentStateTest : BaseStateTest() {

    private lateinit var repository: RecipeRepository
    private lateinit var state: RecipeListState

    override fun doInit() {
        repository = mockk()
        state = ContentState(context, repository)
    }

    @Test
    fun loadsRecipeList() = test {
        val list = listOf(LIST_RECIPE)
        every { repository.recipes } returns listOf<RecipeListLce>(LceState.Loading(), LceState.Content(list)).asFlow()

        state.start(stateMachine)

        @Suppress("UnusedFlow")
        verifyOrder {
            stateMachine.setUiState(RecipeListViewState.Loading)
            repository.recipes
            stateMachine.setUiState(
                RecipeListViewState.Content(
                    state = LceState.Loading(),
                    addEnabled = false,
                    refreshEnabled = false
                )
            )
            stateMachine.setUiState(
                RecipeListViewState.Content(
                    state = LceState.Content(list.toRecipeListItems()),
                    addEnabled = true,
                    refreshEnabled = true
                )
            )
        }
    }

    @Test
    fun terminatesOnBack() = test {
        every { factory.terminated() } returns nextState
        every { repository.recipes } returns flowOf<RecipeListLce>(LceState.Content(emptyList()))

        state.start(stateMachine)
        state.process(RecipeListGesture.Back)

        verify {
            factory.terminated()
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun switchesToAddRecipeOnAddRecipeClick() = test {
        every { factory.addingRecipe() } returns nextState
        every { repository.recipes } returns flowOf<RecipeListLce>(LceState.Content(emptyList()))

        state.start(stateMachine)
        state.process(RecipeListGesture.AddRecipeClicked)

        verify {
            factory.addingRecipe()
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun reloadsDataOnFatalError() = test {
        every { repository.recipes } returns flowOf<RecipeListLce>(LceState.Error(ERROR_FATAL))
        every { repository.synchronizeList() } just Runs

        state.start(stateMachine)
        state.process(RecipeListGesture.DismissError)

        verify { repository.synchronizeList() }
    }

    @Test
    fun reloadsDataOnNonFatalError() = test {
        every { repository.recipes } returns flowOf<RecipeListLce>(LceState.Error(ERROR_NON_FATAL))
        every { repository.synchronizeList() } just Runs

        state.start(stateMachine)
        state.process(RecipeListGesture.DismissError)

        verify { repository.synchronizeList() }
    }
}
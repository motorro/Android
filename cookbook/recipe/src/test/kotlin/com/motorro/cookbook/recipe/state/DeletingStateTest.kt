package com.motorro.cookbook.recipe.state

import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.domain.recipes.RecipeRepository
import com.motorro.cookbook.recipe.data.RecipeFlowData
import com.motorro.cookbook.recipe.data.RecipeViewState
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class DeletingStateTest : BaseStateTest() {

    private lateinit var repository: RecipeRepository
    private lateinit var state: RecipeState

    private val data = RecipeFlowData(ID, LceState.Content(RECIPE))

    override fun doInit() {
        repository = mockk {
            every { deleteRecipe(any()) } just Runs
        }
        every { factory.terminated() } returns nextState

        state = DeletingState(context, data, repository)
    }

    @Test
    fun initialStateIsContentWithLoading() = runTest {
        state.start(stateMachine)

        verify {
            stateMachine.setUiState(RecipeViewState.Content(LceState.Loading(RECIPE), false))
        }
    }

    @Test
    fun deletesRecipeAndMovesToTerminated() = runTest {
        state.start(stateMachine)

        verify {
            repository.deleteRecipe(ID)
            factory.terminated()
            stateMachine.setMachineState(nextState)
        }
    }
}
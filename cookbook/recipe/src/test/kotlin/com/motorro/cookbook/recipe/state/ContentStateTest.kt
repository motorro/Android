package com.motorro.cookbook.recipe.state

import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.domain.recipes.RecipeRepository
import com.motorro.cookbook.recipe.data.RecipeFlowData
import com.motorro.cookbook.recipe.data.RecipeGesture
import com.motorro.cookbook.recipe.data.RecipeViewState
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.flow.flowOf
import org.junit.Test

internal class ContentStateTest : BaseStateTest() {

    private lateinit var repository: RecipeRepository
    private lateinit var state: RecipeState

    override fun doInit() {
        repository = mockk()
        state = ContentState(context, RecipeFlowData(ID), repository)
    }

    @Test
    fun loadsRecipe() = test {
        every { repository.getRecipe(any()) } returns flowOf(LceState.Content(RECIPE))

        state.start(stateMachine)

        @Suppress("UnusedFlow")
        verifyOrder {
            stateMachine.setUiState(RecipeViewState.Content(LceState.Loading(null), false))
            repository.getRecipe(ID)
            stateMachine.setUiState(RecipeViewState.Content(LceState.Content(RECIPE), true))
        }
    }

    @Test
    fun terminatesOnBack() = test {
        every { factory.terminated() } returns nextState
        every { repository.getRecipe(any()) } returns flowOf(LceState.Content(RECIPE))

        state.start(stateMachine)
        state.process(RecipeGesture.Back)

        verify {
            factory.terminated()
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun reloadsDataOnNonCriticalError() = test {
        every { repository.getRecipe(any()) } returns flowOf(LceState.Error(ERROR_NO_NFATAL))
        every { repository.synchronizeRecipe(any()) } just Runs

        state.start(stateMachine)
        state.process(RecipeGesture.DismissError)

        verify { repository.synchronizeRecipe(ID) }
    }

    @Test
    fun transfersToAuthenticationOnAuthError() = test {
        every { repository.getRecipe(any()) } returns flowOf(LceState.Error(ERROR_AUTH))
        every { factory.authenticating(any()) } returns nextState

        state.start(stateMachine)

        verify {
            factory.authenticating(ID)
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun terminatesOnCriticalError() = test {
        every { repository.getRecipe(any()) } returns flowOf(LceState.Error(ERROR_FATAL))
        every { factory.terminated() } returns nextState

        state.start(stateMachine)
        state.process(RecipeGesture.DismissError)

        verify {
            factory.terminated()
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun movesToDeleteConfirmation() = test {
        every { repository.getRecipe(any()) } returns flowOf(LceState.Content(RECIPE))
        every { factory.deleteConfirmation(any()) } returns nextState

        state.start(stateMachine)
        state.process(RecipeGesture.Delete)

        verify {
            factory.deleteConfirmation(RecipeFlowData(ID, LceState.Content(RECIPE)))
            stateMachine.setMachineState(nextState)
        }
    }
}
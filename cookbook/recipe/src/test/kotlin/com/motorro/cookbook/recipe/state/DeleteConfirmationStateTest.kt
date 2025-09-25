package com.motorro.cookbook.recipe.state

import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.recipe.data.RecipeFlowData
import com.motorro.cookbook.recipe.data.RecipeGesture
import com.motorro.cookbook.recipe.data.RecipeViewState
import io.mockk.every
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test

internal class DeleteConfirmationStateTest : BaseStateTest() {

    private lateinit var state: RecipeState
    private val data = RecipeFlowData(ID, LceState.Content(RECIPE))

    override fun doInit() {
        state = DeleteConfirmationState(context, data)
    }

    @Test
    fun displaysConfirmationOnStart() = test {
        state.start(stateMachine)

        verify {
            stateMachine.setUiState(RecipeViewState.DeleteConfirmation(RECIPE))
        }
    }

    @Test
    fun returnsToRecipeOnBack() = test {
        every { factory.content(any()) } returns nextState

        state.start(stateMachine)
        state.process(RecipeGesture.Back)

        verify {
            factory.content(data)
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun returnsToRecipeOnCancelDelete() = test {
        every { factory.content(any()) } returns nextState

        state.start(stateMachine)
        state.process(RecipeGesture.CancelDelete)

        verify {
            factory.content(data)
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun advancesToDeletingOnConfirmDelete() = test {
        every { factory.deleting(any()) } returns nextState

        state.start(stateMachine)
        state.process(RecipeGesture.ConfirmDelete)

        verifyOrder {
            factory.deleting(data)
            stateMachine.setMachineState(nextState)
        }
    }
}
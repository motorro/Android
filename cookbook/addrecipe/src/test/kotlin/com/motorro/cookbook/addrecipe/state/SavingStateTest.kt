package com.motorro.cookbook.addrecipe.state

import com.motorro.cookbook.addrecipe.NEW_RECIPE
import com.motorro.cookbook.addrecipe.data.renderForm
import com.motorro.cookbook.domain.recipes.RecipeRepository
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

internal class SavingStateTest : BaseStateTest() {

    private lateinit var repository: RecipeRepository
    private lateinit var state: AddRecipeState

    override fun doInit() {
        every { factory.terminated() } returns nextState

        repository = mockk {
            every { addRecipe(any()) } just Runs
        }

        state = SavingState(context, NEW_RECIPE, repository)
    }

    @Test
    fun updatesUiState() = test {
        state.start(stateMachine)

        verify {
            stateMachine.setUiState(NEW_RECIPE.renderForm(emptyList(), true))
        }
    }

    @Test
    fun savesAndTerminates() {
        state.start(stateMachine)

        verify {
            repository.addRecipe(NEW_RECIPE)
            factory.terminated()
            stateMachine.setMachineState(nextState)
        }
    }
}
package com.motorro.cookbook.addrecipe.state

import com.motorro.cookbook.addrecipe.NEW_RECIPE
import com.motorro.cookbook.addrecipe.data.AddRecipeGesture
import com.motorro.cookbook.addrecipe.data.AddRecipeViewState
import com.motorro.cookbook.addrecipe.data.filterCategories
import com.motorro.cookbook.addrecipe.data.renderForm
import com.motorro.cookbook.domain.recipes.RecipeRepository
import com.motorro.cookbook.model.RecipeCategory
import com.motorro.cookbook.recipe.state.FormState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Test

internal class FormStateTest : BaseStateTest() {

    private lateinit var repository: RecipeRepository
    private lateinit var state: AddRecipeState

    private val categories = listOf(
        RecipeCategory("Cat"),
        RecipeCategory("Dog"),
        RecipeCategory("Cactus")
    )

    override fun doInit() {
        repository = mockk {
            every { categories } returns flowOf(this@FormStateTest.categories)
        }
        state = FormState(context, repository)
    }

    @Test
    fun displaysEmptyFormOnStart() = test {
        state.start(stateMachine)

        verify {
            stateMachine.setUiState(
                AddRecipeViewState.EMPTY.copy(categories = filterCategories("", categories).map { it.name })
            )
        }
    }

    @Test
    fun displaysFormOnDataChange() = test {
        state.start(stateMachine)
        state.process(AddRecipeGesture.SetTitle(NEW_RECIPE.title))
        state.process(AddRecipeGesture.SetDescription(NEW_RECIPE.description))
        state.process(AddRecipeGesture.SetCategory(NEW_RECIPE.category.name))
        state.process(AddRecipeGesture.SetImage(NEW_RECIPE.image!!.url))

        verify {
            stateMachine.setUiState(
                NEW_RECIPE.renderForm(categories = filterCategories(NEW_RECIPE.category.name, categories))
            )
        }
    }

    @Test
    fun terminatesOnBack() = test {
        every { factory.terminated() } returns nextState

        state.start(stateMachine)
        state.process(AddRecipeGesture.Back)

        verify {
            factory.terminated()
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun proceedsToSaveWhenDataIsValid() = test {
        every { factory.saving(any()) } returns nextState

        state.start(stateMachine)
        state.process(AddRecipeGesture.SetTitle(NEW_RECIPE.title))
        state.process(AddRecipeGesture.SetDescription(NEW_RECIPE.description))
        state.process(AddRecipeGesture.SetCategory(NEW_RECIPE.category.name))
        state.process(AddRecipeGesture.SetImage(NEW_RECIPE.image!!.url))

        state.process(AddRecipeGesture.Save)

        verify {
            factory.saving(NEW_RECIPE)
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun doesNotProceedToSaveIfInvalid() = test {
        state.start(stateMachine)
        state.process(AddRecipeGesture.SetTitle(""))
        state.process(AddRecipeGesture.Save)

        verify(exactly = 0) {
            factory.saving(any())
            stateMachine.setMachineState(any())
        }
    }
}
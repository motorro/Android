package com.motorro.cookbook.recipe.state

import com.motorro.cookbook.addrecipe.data.AddRecipeGesture
import com.motorro.cookbook.addrecipe.data.filterCategories
import com.motorro.cookbook.addrecipe.data.isValid
import com.motorro.cookbook.addrecipe.data.recipe
import com.motorro.cookbook.addrecipe.data.recipeFlow
import com.motorro.cookbook.addrecipe.data.renderForm
import com.motorro.cookbook.addrecipe.data.saveCategory
import com.motorro.cookbook.addrecipe.data.saveDescription
import com.motorro.cookbook.addrecipe.data.saveImage
import com.motorro.cookbook.addrecipe.data.saveTitle
import com.motorro.cookbook.addrecipe.state.AddRecipeContext
import com.motorro.cookbook.addrecipe.state.AddRecipeState
import com.motorro.cookbook.domain.recipes.RecipeRepository
import com.motorro.cookbook.domain.recipes.data.NewRecipe
import com.motorro.cookbook.model.RecipeCategory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 * Recipe content
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class FormState(
    context: AddRecipeContext,
    private val repository: RecipeRepository
) : AddRecipeState(context) {

    override fun doStart() {
        super.doStart()
        subscribeData()
    }

    /**
     * Subscribes data from saved state handle
     */
    fun subscribeData() {
        savedStateHandle.recipeFlow(stateScope).flatMapLatest { recipe ->
            repository.categories
                .map { filterCategories(recipe.category.name, it) }
                .onStart { emit(emptyList()) }
                .map { recipe to it }
        }.onEach(::render).launchIn(stateScope)
    }

    override fun doProcess(gesture: AddRecipeGesture) {
        when(gesture) {
            AddRecipeGesture.Back -> {
                d { "Back is pressed. Terminating..." }
                setMachineState(factory.terminated())
            }
            AddRecipeGesture.Save -> {
                val toSave = savedStateHandle.recipe()
                if (toSave.isValid()) {
                    d { "Save requested. Transferring to saving..." }
                    setMachineState(factory.saving(toSave))
                }
            }
            is AddRecipeGesture.SetCategory -> savedStateHandle.saveCategory(gesture.category)
            is AddRecipeGesture.SetDescription -> savedStateHandle.saveDescription(gesture.description)
            is AddRecipeGesture.SetImage -> savedStateHandle.saveImage(gesture.image)
            is AddRecipeGesture.SetTitle -> savedStateHandle.saveTitle(gesture.title)
        }
    }

    /**
     * Renders recipe form
     */
    fun render(data: Pair<NewRecipe, List<RecipeCategory>>) {
        setUiState(data.first.renderForm(data.second))
    }

    /**
     * Factory for [FormState]
     */
    class Factory @Inject constructor(private val repository: RecipeRepository) {
        operator fun invoke(context: AddRecipeContext) = FormState(
            context,
            repository
        )
    }
}
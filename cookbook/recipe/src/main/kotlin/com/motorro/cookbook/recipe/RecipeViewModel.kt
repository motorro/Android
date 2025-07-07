package com.motorro.cookbook.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.domain.recipes.RecipeRepository
import com.motorro.cookbook.domain.recipes.data.RecipeLce
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlin.uuid.Uuid

@HiltViewModel(assistedFactory = RecipeViewModel.Factory::class)
class RecipeViewModel @AssistedInject constructor(
    private val repository: RecipeRepository,
    @Assisted private val recipeId: Uuid
) : ViewModel() {

    /**
     * Recipe to display
     */
    val recipe: StateFlow<RecipeLce> get() = repository.getRecipe(recipeId).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        LceState.Loading()
    )

    /**
     * Refreshes data
     */
    fun refresh() = repository.synchronizeRecipe(recipeId)

    /**
     * Deletes recipe
     */
    fun deleteRecipe() {
        repository.deleteRecipe(recipeId)
    }

    @AssistedFactory
    interface Factory {
        fun create(recipeId: Uuid): RecipeViewModel
    }
}
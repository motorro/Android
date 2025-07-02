package com.motorro.cookbook.recipe

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.motorro.cookbook.appcore.di.DiContainer
import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.domain.recipes.RecipeRepository
import com.motorro.cookbook.domain.recipes.data.RecipeLce
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlin.uuid.Uuid

class RecipeViewModel(private val repository: RecipeRepository, private val recipeId: Uuid) : ViewModel() {

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

    @Suppress("UNCHECKED_CAST")
    class Factory(context: Context, private val recipeId: Uuid) : ViewModelProvider.Factory {

        private val container = context.applicationContext as DiContainer

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RecipeViewModel(container.recipeRepository, recipeId) as T
        }
    }
}
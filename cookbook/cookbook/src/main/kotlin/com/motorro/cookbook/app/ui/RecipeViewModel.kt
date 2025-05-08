package com.motorro.cookbook.app.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.motorro.cookbook.app.App
import com.motorro.cookbook.app.data.RecipeLce
import com.motorro.cookbook.app.repository.RecipeRepository
import com.motorro.core.lce.LceState
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

        private val app: App = context.applicationContext as App

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RecipeViewModel(app.recipeRepository, recipeId) as T
        }
    }
}
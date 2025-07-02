package com.motorro.cookbook.recipelist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.motorro.cookbook.appcore.di.DiContainer
import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.domain.recipes.RecipeRepository
import com.motorro.cookbook.domain.session.SessionManager
import com.motorro.cookbook.recipelist.data.RecipeListItemLce
import com.motorro.cookbook.recipelist.data.getRecipeList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecipeListViewModel(
    private val sessionManager: SessionManager,
    private val recipeRepository: RecipeRepository
) : ViewModel() {
    /**
     * List of recipes
     */
    val recipes: StateFlow<RecipeListItemLce> = recipeRepository.getRecipeList().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        LceState.Loading()
    )

    /**
     * Refreshes data
     */
    fun refresh() = recipeRepository.synchronizeList()

    /**
     * Logs out user
     */
    fun logout() = viewModelScope.launch {
        sessionManager.logout()
    }

    class Factory(context: Context) : ViewModelProvider.Factory {

        private val container = context.applicationContext as DiContainer

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RecipeListViewModel(container.sessionManager, container.recipeRepository) as T
        }
    }
}
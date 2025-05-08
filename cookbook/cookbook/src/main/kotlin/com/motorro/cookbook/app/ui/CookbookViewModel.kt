package com.motorro.cookbook.app.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.motorro.cookbook.app.App
import com.motorro.cookbook.app.data.RecipeListItemLce
import com.motorro.cookbook.app.repository.RecipeRepository
import com.motorro.cookbook.app.repository.getRecipeList
import com.motorro.cookbook.app.session.SessionManager
import com.motorro.core.lce.LceState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CookbookViewModel(
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

        private val app: App = context.applicationContext as App

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CookbookViewModel(app.sessionManager, app.recipeRepository) as T
        }
    }
}
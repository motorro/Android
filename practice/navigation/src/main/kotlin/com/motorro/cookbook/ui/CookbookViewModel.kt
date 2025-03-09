package com.motorro.cookbook.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.motorro.cookbook.App
import com.motorro.cookbook.data.RecipeListItem
import com.motorro.cookbook.data.RecipeRepository
import com.motorro.cookbook.data.getRecipeList

class CookbookViewModel(private val repository: RecipeRepository) : ViewModel() {
    /**
     * Filtered list of recipes
     */
    val recipes: LiveData<List<RecipeListItem>> get() = repository.getRecipeList()

    class Factory(context: Context) : ViewModelProvider.Factory {

        private val app: App = context.applicationContext as App

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CookbookViewModel(app.recipeRepository) as T
        }
    }
}
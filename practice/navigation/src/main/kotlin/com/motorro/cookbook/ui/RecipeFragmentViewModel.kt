package com.motorro.cookbook.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.motorro.cookbook.App
import com.motorro.cookbook.data.Recipe
import com.motorro.cookbook.data.RecipeRepository

class RecipeFragmentViewModel(private val repository: RecipeRepository, private val recipeId: Int) : ViewModel() {

    /**
     * Recipe to display
     */
    val recipe: LiveData<Recipe> get() = MutableLiveData(repository.getRecipe(recipeId))

    /**
     * Deletes recipe
     */
    fun deleteRecipe() {
        repository.deleteRecipe(recipeId)
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(context: Context, private val recipeId: Int) : ViewModelProvider.Factory {

        private val app: App = context.applicationContext as App

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RecipeFragmentViewModel(app.recipeRepository, recipeId) as T
        }
    }
}
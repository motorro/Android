package com.motorro.cookbook.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.motorro.cookbook.App
import com.motorro.cookbook.data.RecipeCategory
import com.motorro.cookbook.data.RecipeFilter
import com.motorro.cookbook.data.RecipeListItem
import com.motorro.cookbook.data.RecipeRepository
import com.motorro.cookbook.data.getRecipeList
import com.motorro.cookbook.update

class CookbookViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {
    /**
     * Filter to apply to recipes
     */
    private val filter = MutableLiveData(RecipeFilter())

    /**
     * Filtered list of recipes
     */
    val recipes: LiveData<List<RecipeListItem>> get() = filter.distinctUntilChanged().switchMap {
        recipeRepository.getRecipeList(filter = it)
    }

    /**
     * List of categories
     */
    val categories: LiveData<List<Pair<RecipeCategory, Boolean>>> get() = filter.distinctUntilChanged().switchMap { filter ->
        recipeRepository.categories.map { categories -> categories.map { it to (it in filter.categories) } }
    }

    /**
     * Query to filter recipes
     */
    val query: LiveData<String> get() = filter.distinctUntilChanged().map {
        it.query.orEmpty()
    }

    /**
     * Updates query to filter recipes
     */
    fun setQuery(query: String) {
        filter.update { it.copy(query = query) }
    }

    /**
     * Toggles category to filter recipes
     */
    fun toggleCategory(category: RecipeCategory) {
        filter.update {
            val categories = it.categories.toMutableSet()
            if (category in categories) {
                categories.remove(category)
            } else {
                categories.add(category)
            }
            it.copy(categories = categories)
        }
    }

    class Factory(context: Context) : ViewModelProvider.Factory {

        private val app: App = context.applicationContext as App

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CookbookViewModel(app.recipeRepository) as T
        }
    }
}
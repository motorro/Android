package com.motorro.cookbook.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.motorro.cookbook.App
import com.motorro.cookbook.data.Recipe
import com.motorro.cookbook.data.RecipeCategory
import com.motorro.cookbook.data.RecipeRepository

class AddRecipeFragmentViewModel(private val repository: RecipeRepository) : ViewModel() {

    val title: LiveData<String> get() = TODO("Implement title live data")
    val image: LiveData<String?> get() = TODO("Implement image live data")
    val category: LiveData<String> get() = TODO("Implement category live data")

    val categories: LiveData<List<String>> get() = TODO("Implement categories live data")

    val steps: LiveData<String> get() = TODO("Implement steps live data")

    val saveEnabled: LiveData<Boolean> get() = TODO("Implement save enabled live data")

    fun setTitle(title: String) {
        TODO("Implement setTitle")
    }

    fun setImage(image: String?) {
        TODO("Implement setImage")
    }

    fun setCategory(category: String) {
        TODO("Implement setCategory")
    }

    fun setSteps(steps: String) {
        TODO("Implement setSteps")
    }

    fun save() {
        val title = title.value ?: return
        val image = image.value
        val category = category.value ?: return
        val steps = steps.value ?: return

        repository.addRecipe(Recipe(
            id = 0,
            title = title,
            imageUrl = image,
            category = RecipeCategory(category),
            steps = steps.split('\n')
        ))
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(context: Context) : ViewModelProvider.Factory {

        private val app: App = context.applicationContext as App

        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            return AddRecipeFragmentViewModel(app.recipeRepository) as T
        }
    }
}
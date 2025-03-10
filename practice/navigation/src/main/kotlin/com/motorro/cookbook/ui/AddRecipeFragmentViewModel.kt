package com.motorro.cookbook.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.motorro.cookbook.App
import com.motorro.cookbook.data.Recipe
import com.motorro.cookbook.data.RecipeCategory
import com.motorro.cookbook.data.RecipeRepository

class AddRecipeFragmentViewModel(private val repository: RecipeRepository, private val savedStateHandle: SavedStateHandle) : ViewModel() {

    val title: LiveData<String> get() = savedStateHandle.getLiveData<String>(KEY_TITLE).distinctUntilChanged()
    val image: LiveData<String?> get() = savedStateHandle.getLiveData<String>(KEY_IMAGE).distinctUntilChanged()
    val category: LiveData<String> get() = savedStateHandle.getLiveData<String>(KEY_CATEGORY).distinctUntilChanged()

    val categories: LiveData<List<String>> get() = MediatorLiveData<List<String>>().apply {
        fun filterCategories(selectedCategory: String?, categories: List<RecipeCategory>?): List<String> {
            val filtered = if (null != selectedCategory) {
                categories.orEmpty().filter{ category -> category.name.startsWith(selectedCategory, ignoreCase = true) }
            } else {
                categories.orEmpty()
            }

            return filtered.map { it.name }
        }

        addSource(category) { selectedCategory ->
            value = filterCategories(selectedCategory, repository.categories.value)
        }
        addSource(repository.categories) { categories ->
            value = filterCategories(category.value, categories)
        }
    }

    val steps: LiveData<String> get() = savedStateHandle.getLiveData<String>(KEY_STEPS).distinctUntilChanged()

    val saveEnabled: LiveData<Boolean> get() = MediatorLiveData(false).apply {
        fun canSave(title: String?, category: String?, steps: String?): Boolean {
            return (title.isNullOrEmpty() || category.isNullOrEmpty() || steps.isNullOrEmpty()).not()
        }

        addSource(title) { title ->
            value = canSave(title, category.value, steps.value)
        }
        addSource(category) { category ->
            value = canSave(title.value, category, steps.value)
        }
        addSource(steps) { steps ->
            value = canSave(title.value, category.value, steps)
        }
    }

    fun setTitle(title: String) {
        savedStateHandle[KEY_TITLE] = title
    }

    fun setImage(image: String?) {
        savedStateHandle[KEY_IMAGE] = image
    }

    fun setCategory(category: String) {
        savedStateHandle[KEY_CATEGORY] = category
    }

    fun setSteps(steps: String) {
        savedStateHandle[KEY_STEPS] = steps
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

    private companion object {
        const val KEY_TITLE = "title"
        const val KEY_IMAGE = "image"
        const val KEY_CATEGORY = "category"
        const val KEY_STEPS = "steps"
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(context: Context) : ViewModelProvider.Factory {

        private val app: App = context.applicationContext as App

        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            return AddRecipeFragmentViewModel(app.recipeRepository, extras.createSavedStateHandle()) as T
        }
    }
}
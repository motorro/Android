package com.motorro.cookbook.app.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.motorro.cookbook.app.App
import com.motorro.cookbook.app.data.NewRecipe
import com.motorro.cookbook.app.repository.RecipeRepository
import com.motorro.cookbook.data.RecipeCategory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class AddRecipeFragmentViewModel(private val repository: RecipeRepository, private val savedStateHandle: SavedStateHandle) : ViewModel() {

    val title: StateFlow<String> get() = savedStateHandle.getStateFlow(KEY_TITLE, "")
    val image: StateFlow<Uri?> get() = savedStateHandle.getStateFlow(KEY_IMAGE, null)
    val category: StateFlow<String> get() = savedStateHandle.getStateFlow(KEY_CATEGORY, "")

    val categories: StateFlow<List<String>> get() = category.flatMapLatest { categoryValue ->

        fun filterCategories(selectedCategory: String?, categories: List<RecipeCategory>?): List<String> {
            val filtered = if (null != selectedCategory) {
                categories.orEmpty().filter{ category -> category.name.startsWith(selectedCategory, ignoreCase = true) }
            } else {
                categories.orEmpty()
            }

            return filtered.map { it.name }
        }

        repository.categories.map { filterCategories(categoryValue, it) }

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val description: StateFlow<String> get() = savedStateHandle.getStateFlow(KEY_STEPS, "")

    val saveEnabled: StateFlow<Boolean> get() = combine(title, category, description) { t, c, d ->
        (t.isBlank() || c.isBlank() || d.isBlank()).not()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    fun setTitle(title: String) {
        savedStateHandle[KEY_TITLE] = title
    }

    fun setImage(image: Uri?) {
        savedStateHandle[KEY_IMAGE] = image
    }

    fun setCategory(category: String) {
        savedStateHandle[KEY_CATEGORY] = category
    }

    fun setSteps(steps: String) {
        savedStateHandle[KEY_STEPS] = steps
    }

    fun save() {
        val title = title.value.takeIf { it.isNotBlank() } ?: return
        val image = image.value
        val category = category.value.takeIf { it.isNotBlank() } ?: return
        val description = description.value.takeIf { it.isNotBlank() } ?: return

        repository.addRecipe(NewRecipe(
            title = title,
            category = RecipeCategory(category),
            description = description,
            image = image
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
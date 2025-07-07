package com.motorro.cookbook.addrecipe

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.motorro.cookbook.domain.recipes.RecipeRepository
import com.motorro.cookbook.domain.recipes.data.NewRecipe
import com.motorro.cookbook.model.Image
import com.motorro.cookbook.model.RecipeCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class AddRecipeFragmentViewModel @Inject constructor(
    private val repository: RecipeRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

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

        repository.addRecipe(
            NewRecipe(
                title = title,
                category = RecipeCategory(category),
                description = description,
                image = Image(image.toString())
            )
        )
    }

    private companion object {
        const val KEY_TITLE = "title"
        const val KEY_IMAGE = "image"
        const val KEY_CATEGORY = "category"
        const val KEY_STEPS = "steps"
    }
}
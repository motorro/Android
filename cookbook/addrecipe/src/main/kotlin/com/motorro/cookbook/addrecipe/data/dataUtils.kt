package com.motorro.cookbook.addrecipe.data

import androidx.lifecycle.SavedStateHandle
import com.motorro.cookbook.domain.recipes.data.NewRecipe
import com.motorro.cookbook.model.Image
import com.motorro.cookbook.model.RecipeCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

private const val KEY_TITLE = "title"
private const val KEY_IMAGE = "image"
private const val KEY_CATEGORY = "category"
private const val KEY_STEPS = "steps"

/**
 * Creates [NewRecipe] from raw data
 */
private fun NewRecipe(title: String, image: String?, category: String, description: String) = NewRecipe(
    title = title,
    image = image?.let(::Image),
    category = RecipeCategory(category),
    description = description
)

/**
 * Gets [NewRecipe] from [SavedStateHandle]
 */
internal fun SavedStateHandle.recipe(): NewRecipe = NewRecipe(
    title = get<String>(KEY_TITLE).orEmpty(),
    image = get<String>(KEY_IMAGE),
    category = get<String>(KEY_CATEGORY).orEmpty(),
    description = get<String>(KEY_STEPS).orEmpty()
)

/**
 * Gets [NewRecipe] from [SavedStateHandle]
 */
internal fun SavedStateHandle.recipeFlow(scope: CoroutineScope): StateFlow<NewRecipe> = combine(
    getStateFlow(KEY_TITLE, ""),
    getStateFlow<String?>(KEY_IMAGE, null),
    getStateFlow(KEY_CATEGORY, ""),
    getStateFlow(KEY_STEPS, ""),
    transform = ::NewRecipe
).stateIn(scope, SharingStarted.Lazily, recipe())

/**
 * Cleans up [SavedStateHandle] from recipe data
 */
internal fun SavedStateHandle.cleanup() {
    remove<String>(KEY_TITLE)
    remove<String?>(KEY_IMAGE)
    remove<String>(KEY_CATEGORY)
    remove<String>(KEY_STEPS)
}

/**
 * Saves recipe title
 */
internal fun SavedStateHandle.saveTitle(title: String) {
    set(KEY_TITLE, title)
}

/**
 * Saves recipe image
 */
internal fun SavedStateHandle.saveImage(image: String?) {
    set(KEY_IMAGE, image)
}

/**
 * Save recipe category
 */
internal fun SavedStateHandle.saveCategory(category: String) {
    set(KEY_CATEGORY, category)
}

/**
 * Filters categories that match [selectedCategory]
 */
internal fun filterCategories(selectedCategory: String, categories: List<RecipeCategory>?): List<RecipeCategory> = if (selectedCategory.isBlank()) {
    categories.orEmpty()
} else {
    categories.orEmpty().filter { category -> category.name.startsWith(selectedCategory, ignoreCase = true) }
}

/**
 * Saves recipe description
 */
internal fun SavedStateHandle.saveDescription(description: String) {
    set(KEY_STEPS, description)
}

/**
 * Checks if [NewRecipe] is valid
 */
internal fun NewRecipe.isValid(): Boolean = title.isNotBlank() && category.name.isNotBlank() && description.isNotBlank()

/**
 * Renders [NewRecipe] to [AddRecipeViewState.Form]
 */
internal fun NewRecipe.renderForm(categories: List<RecipeCategory>, saving: Boolean = false): AddRecipeViewState.Form = AddRecipeViewState.Form(
    title = title,
    image = image?.url,
    category = category.name,
    categories = categories.map { it.name },
    description = description,
    isValid = isValid(),
    saving = saving
)
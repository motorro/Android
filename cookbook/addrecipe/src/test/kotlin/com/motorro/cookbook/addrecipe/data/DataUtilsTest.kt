package com.motorro.cookbook.addrecipe.data

import androidx.lifecycle.SavedStateHandle
import com.motorro.cookbook.addrecipe.NEW_RECIPE
import com.motorro.cookbook.domain.recipes.data.NewRecipe
import com.motorro.cookbook.model.RecipeCategory
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.util.LinkedList
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class DataUtilsTest {

    @Test
    fun savesAndRestoresNewRecipe() = runTest(UnconfinedTestDispatcher()) {
        val emptyRecipe = NewRecipe(
            title = "",
            image = null,
            category = RecipeCategory(""),
            description = ""
        )
        val savedStateHandle = SavedStateHandle()

        val recipes = LinkedList<NewRecipe>()
        val j = backgroundScope.launch {
            savedStateHandle.recipeFlow(this).collect {
                recipes.add(it)
            }
        }

        assertEquals(emptyRecipe, savedStateHandle.recipe())

        savedStateHandle.saveTitle(NEW_RECIPE.title)
        savedStateHandle.saveImage(NEW_RECIPE.image!!.url)
        savedStateHandle.saveCategory(NEW_RECIPE.category.name)
        savedStateHandle.saveDescription(NEW_RECIPE.description)

        assertEquals(NEW_RECIPE, savedStateHandle.recipe())
    }

    @Test
    fun returnsAllCategoriesForEmptyString() {
        val categories = listOf(
            RecipeCategory("Cat"),
            RecipeCategory("Dog"),
            RecipeCategory("Cactus")
        )

        val filtered = filterCategories("", categories)

        assertEquals(categories, filtered)
    }

    @Test
    fun returnsCategoriesThatMatchPrefix() {
        val categories = listOf(
            RecipeCategory("Cat"),
            RecipeCategory("Dog"),
            RecipeCategory("Cactus")
        )

        val filtered = filterCategories("ca", categories)

        assertEquals(listOf(categories[0], categories[2]), filtered)
    }

    @Test
    fun newRecipeIsValidWhenTitleDescriptionAndCategoryAreNotEmpty() {
        val newRecipe = NEW_RECIPE.copy(
            title = "Title",
            description = "Description",
            category = RecipeCategory("Category")
        )
        assertTrue(newRecipe.isValid())
    }

    @Test
    fun newRecipeIsNotValidWhenTitleIsEmpty() {
        val newRecipe = NEW_RECIPE.copy(title = "")
        assertFalse(newRecipe.isValid())
    }

    @Test
    fun newRecipeIsNotValidWhenDescriptionIsEmpty() {
        val newRecipe = NEW_RECIPE.copy(description = "")
        assertFalse(newRecipe.isValid())
    }

    @Test
    fun newRecipeIsNotValidWhenCategoryIsEmpty() {
        val newRecipe = NEW_RECIPE.copy(category = RecipeCategory(""))
        assertFalse(newRecipe.isValid())
    }

    @Test
    fun rendersFormStateFromNewRecipe() {
        val newRecipe = NEW_RECIPE
        val form = newRecipe.renderForm(listOf(newRecipe.category))
        assertEquals(true, form.isValid)
        assertEquals(NEW_RECIPE.title, form.title)
        assertEquals(NEW_RECIPE.description, form.description)
        assertEquals(NEW_RECIPE.category.name, form.category)
        assertEquals(NEW_RECIPE.image!!.url, form.image)
        assertEquals(listOf(newRecipe.category.name), form.categories)
    }
}
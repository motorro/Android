package com.motorro.cookbook.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.motorro.cookbook.data.Recipe
import com.motorro.cookbook.data.RecipeListItem
import com.motorro.cookbook.data.RecipeRepository
import com.motorro.cookbook.data.toRecipeListItems
import com.motorro.cookbook.mockRecipes
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CookbookViewModelTest {

    private lateinit var recipes: MutableLiveData<List<Recipe>>
    private lateinit var repository: RecipeRepository
    private lateinit var model: CookbookViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        recipes = MutableLiveData()
        repository = mockk {
            every { getRecipes(any()) } returns this@CookbookViewModelTest.recipes
        }
        model = CookbookViewModel(repository)
    }


    @Test
    fun startsWithEmptyFilter() {
        val recipesObserver: Observer<List<RecipeListItem>> = mockk(relaxed = true)
        model.recipes.observeForever(recipesObserver)

        recipes.value = mockRecipes

        verify {
            recipesObserver.onChanged(mockRecipes.toRecipeListItems())
        }
    }
}
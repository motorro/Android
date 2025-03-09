package com.motorro.cookbook.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.motorro.cookbook.data.Recipe
import com.motorro.cookbook.data.RecipeCategory
import com.motorro.cookbook.data.RecipeFilter
import com.motorro.cookbook.data.RecipeListItem
import com.motorro.cookbook.data.RecipeRepository
import com.motorro.cookbook.data.toRecipeListItems
import com.motorro.cookbook.mockRecipes
import io.mockk.Ordering
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CookbookViewModelTest {

    private lateinit var recipes: MutableLiveData<List<Recipe>>
    private lateinit var categories: MutableLiveData<List<RecipeCategory>>
    private lateinit var repository: RecipeRepository
    private lateinit var model: CookbookViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        recipes = MutableLiveData(mockRecipes)
        categories = MutableLiveData(mockRecipes.map { it.category }.distinct())
        repository = mockk {
            every { getRecipes(any()) } returns this@CookbookViewModelTest.recipes
            every { categories } returns this@CookbookViewModelTest.categories
        }
        model = CookbookViewModel(repository)
    }

    @Test
    fun startsWithEmptyFilter() {
        val recipesObserver: Observer<List<RecipeListItem>> = mockk(relaxed = true)
        model.recipes.observeForever(recipesObserver)

        verify {
            recipesObserver.onChanged(mockRecipes.toRecipeListItems())
        }
    }

    @Test
    fun allCategoriesAreTurnedOffByDefault() {
        val categoryObserver: Observer<List<Pair<RecipeCategory, Boolean>>> = mockk(relaxed = true)
        model.categories.observeForever(categoryObserver)

        verify {
            categoryObserver.onChanged(mockRecipes.map { it.category }.distinct().map { it to false })
        }
    }

    @Test
    fun updatesFilterWhenQueryIsSet() {
        val queryObserver: Observer<String> = mockk(relaxed = true)
        model.query.observeForever(queryObserver)

        model.setQuery("bread")

        verify(ordering = Ordering.ORDERED) {
            queryObserver.onChanged("")
            queryObserver.onChanged("bread")
        }
    }

    @Test
    fun filtersByQuery() {
        val recipesObserver: Observer<List<RecipeListItem>> = mockk(relaxed = true)
        model.recipes.observeForever(recipesObserver)

        model.setQuery("bread")

        verify(ordering = Ordering.ORDERED) {
            repository.getRecipes(RecipeFilter())
            repository.getRecipes(RecipeFilter(query = "bread"))
        }
    }

    @Test
    fun togglesCategory() {
        val categoryObserver: Observer<List<Pair<RecipeCategory, Boolean>>> = mockk(relaxed = true)
        model.categories.observeForever(categoryObserver)

        model.toggleCategory(mockRecipes[0].category)
        model.toggleCategory(mockRecipes[0].category)

        verify(ordering = Ordering.ORDERED) {
            categoryObserver.onChanged(mockRecipes.map { it.category }.distinct().map { it to false })
            categoryObserver.onChanged(mockRecipes.map { it.category }.distinct().map { it to (it == mockRecipes[0].category) })
            categoryObserver.onChanged(mockRecipes.map { it.category }.distinct().map { it to false })
        }
    }

    @Test
    fun filtersByCategory() {
        val recipesObserver: Observer<List<RecipeListItem>> = mockk(relaxed = true)
        model.recipes.observeForever(recipesObserver)

        model.toggleCategory(mockRecipes[0].category)
        model.toggleCategory(mockRecipes[0].category)

        verify(ordering = Ordering.ORDERED) {
            repository.getRecipes(RecipeFilter())
            repository.getRecipes(RecipeFilter(categories = setOf((mockRecipes[0].category))))
            repository.getRecipes(RecipeFilter())
        }
    }
}
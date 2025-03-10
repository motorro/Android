package com.motorro.cookbook.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.motorro.cookbook.data.RecipeRepository
import com.motorro.cookbook.mockRecipes
import io.mockk.Ordering
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddRecipeFragmentViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: RecipeRepository
    private lateinit var model: AddRecipeFragmentViewModel

    @Before
    fun init() {
        repository = mockk {
            every { categories } returns MutableLiveData(mockRecipes.map { it.category })
        }
        model = AddRecipeFragmentViewModel(repository, SavedStateHandle())
    }

    @Test
    fun updatesTitle() {
        val titleObserver: Observer<String> = mockk(relaxed = true)
        model.title.observeForever(titleObserver)

        model.setTitle("Title")

        verify {
            titleObserver.onChanged("Title")
        }
    }

    @Test
    fun updatesImage() {
        val imageObserver: Observer<String?> = mockk(relaxed = true)
        model.image.observeForever(imageObserver)

        model.setImage("https://example.com/image.jpg")

        verify {
            imageObserver.onChanged("https://example.com/image.jpg")
        }
    }

    @Test
    fun updatesCategory() {
        val categoryObserver: Observer<String?> = mockk(relaxed = true)
        model.category.observeForever(categoryObserver)

        model.setCategory("Category 1")

        verify {
            categoryObserver.onChanged("Category 1")
        }
    }

    @Test
    fun updatesCategories() {
        val categoriesObserver: Observer<List<String>> = mockk(relaxed = true)
        model.categories.observeForever(categoriesObserver)

        model.setCategory("Category")
        model.setCategory("Category 1")

        verify(ordering = Ordering.ORDERED) {
            categoriesObserver.onChanged(listOf(
                "Category 1",
                "Category 2",
                "Category 3"
            ))
            categoriesObserver.onChanged(listOf(
                "Category 1"
            ))
        }
    }

    @Test
    fun updatesSteps() {
        val stepsObserver: Observer<String> = mockk(relaxed = true)
        model.steps.observeForever(stepsObserver)

        model.setSteps("Step 1\nStep 2")

        verify {
            stepsObserver.onChanged("Step 1\nStep 2")
        }
    }

    @Test
    fun enablesSaveWhenTitleCategoryAndStepsAreSet() {
        val saveEnabledObserver: Observer<Boolean> = mockk(relaxed = true)
        model.saveEnabled.observeForever(saveEnabledObserver)

        model.setTitle("Title")
        model.setCategory("Category 1")
        model.setSteps("Step 1\nStep 2")

        verify {
            saveEnabledObserver.onChanged(true)
        }
    }
}
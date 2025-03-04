package com.motorro.cookbook.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import com.motorro.cookbook.update

/**
 * Provides access to the list of recipes.
 */
interface RecipeRepository {
    /**
     * Returns the list of recipes as a live data.
     * @param filter Optional recipe filter
     */
    fun getRecipes(filter: RecipeFilter): LiveData<List<Recipe>>

    /**
     * Returns the recipe with the specified ID.
     * @param id Recipe ID
     */
    fun getRecipe(id: Int): Recipe?

    /**
     * A list of categories
     */
    val categories: LiveData<List<RecipeCategory>>

    /**
     * Adds new recipe
     * @param recipe Recipe to add
     */
    fun addRecipe(recipe: Recipe)

    /**
     * Deletes recipe
     * @param id Recipe ID to delete
     */
    fun deleteRecipe(id: Int)

    /**
     * [RecipeRepository] implementation
     */
    class Impl(recipes: List<Recipe>) : RecipeRepository {

        private val nextId = recipes.maxOfOrNull { it.id }?.plus(1) ?: 1
        private val recipes = MutableLiveData(recipes)

        override fun getRecipes(filter: RecipeFilter): LiveData<List<Recipe>> = recipes
            .map { recipes -> recipes.asSequence()
                .filter { filter.categories.isEmpty() || filter.categories.contains(it.category) }
                .filter { filter.query.isNullOrBlank() || it.title.contains(filter.query, ignoreCase = true) }
                .toList()
            }
            .distinctUntilChanged()

        override val categories: LiveData<List<RecipeCategory>> get() = recipes
            .map { recipes ->  recipes.map { it.category }.distinct().sorted() }
            .distinctUntilChanged()

        /**
         * Returns the recipe with the specified ID.
         */
        override fun getRecipe(id: Int): Recipe? = recipes.value?.find { it.id == id }

        /**
         * Returns the list of recipes as a flow.
         * @param id The ID of the recipe to delete.
         */
        override fun deleteRecipe(id: Int) {
            recipes.update { list ->
                list.filter { it.id != id }
            }
        }

        /**
         * Adds a recipe to the list.
         * @param recipe The recipe to add.
         */
        override fun addRecipe(recipe: Recipe) {
            recipes.update { list ->
                list + recipe.copy(id = nextId)
            }
        }
    }
}


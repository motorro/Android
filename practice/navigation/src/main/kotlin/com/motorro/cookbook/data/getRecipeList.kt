package com.motorro.cookbook.data

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.motorro.cookbook.data.RecipeListItem.CategoryItem
import com.motorro.cookbook.data.RecipeListItem.RecipeItem

/**
 * Transforms the list of recipes into a list of recipe list items.
 * @param filter Filter to apply
 */
fun RecipeRepository.getRecipeList(filter: RecipeFilter = RecipeFilter()): LiveData<List<RecipeListItem>> = getRecipes(filter).map { recipes ->
    recipes.toRecipeListItems()
}

/**
 * Converts the list of recipes into a list of recipe list items.
 */
@VisibleForTesting
fun List<Recipe>.toRecipeListItems(): List<RecipeListItem> =
    groupBy { it.category }
        .entries
        .sortedBy { it.key }
        .map { (category, recipes) ->
            listOf(CategoryItem(category)) + recipes.map { RecipeItem(it) }.sortedBy { it.title }
        }
        .flatten()
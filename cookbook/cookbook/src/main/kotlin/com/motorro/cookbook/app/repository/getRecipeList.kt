package com.motorro.cookbook.app.repository

import com.motorro.cookbook.app.data.RecipeListItem
import com.motorro.cookbook.app.data.RecipeListItem.CategoryItem
import com.motorro.cookbook.app.data.RecipeListItem.RecipeItem
import com.motorro.cookbook.app.data.RecipeListItemLce
import com.motorro.cookbook.data.ListRecipe
import com.motorro.core.lce.LceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Transforms the list of recipes into a list of recipe list items.
 * @param filter Filter to apply
 */
fun RecipeRepository.getRecipeList(): Flow<RecipeListItemLce> = recipes.map { recipesState ->
    when (recipesState) {
        is LceState.Content -> LceState.Content(recipesState.data.toRecipeListItems())
        is LceState.Error -> LceState.Error(recipesState.error, recipesState.data?.toRecipeListItems())
        is LceState.Loading -> LceState.Loading(recipesState.data?.toRecipeListItems())
    }
}

/**
 * Converts the list of recipes into a list of recipe list items.
 */
fun List<ListRecipe>.toRecipeListItems(): List<RecipeListItem> =
    groupBy { it.category }
        .entries
        .sortedBy { it.key }
        .map { (category, recipes) ->
            listOf(CategoryItem(category)) + recipes.map { RecipeItem(it) }.sortedBy { it.title }
        }
        .flatten()
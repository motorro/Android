package com.motorro.cookbook.app.data

import com.motorro.cookbook.data.ListRecipe
import com.motorro.cookbook.data.Recipe
import com.motorro.core.lce.LceState

typealias RecipeListLce = LceState<List<ListRecipe>, CookbookError>
typealias RecipeListItemLce = LceState<List<RecipeListItem>, CookbookError>
typealias RecipeLce = LceState<Recipe, CookbookError>
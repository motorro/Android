package com.motorro.cookbook.recipelist.data

import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.domain.recipes.data.RecipeListLce

/**
 * Inter-state data for recipe-list flow
 */
data class RecipeFlowData(val data: RecipeListLce = LceState.Loading(null))
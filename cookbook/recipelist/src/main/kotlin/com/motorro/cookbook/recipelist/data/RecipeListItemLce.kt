package com.motorro.cookbook.recipelist.data

import com.motorro.cookbook.core.error.CoreException
import com.motorro.cookbook.core.lce.LceState

typealias RecipeListItemLce = LceState<List<RecipeListItem>, CoreException>

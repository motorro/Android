package com.motorro.cookbook.app.data

import android.net.Uri
import com.motorro.cookbook.data.RecipeCategory

/**
 * New recipe data
 */
data class NewRecipe(val title: String, val category: RecipeCategory, val description: String, val image: Uri?)
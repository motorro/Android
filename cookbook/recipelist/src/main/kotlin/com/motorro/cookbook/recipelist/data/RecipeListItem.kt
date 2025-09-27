package com.motorro.cookbook.recipelist.data

import com.motorro.cookbook.appcore.ui.WithTypeId
import com.motorro.cookbook.appcore.ui.WithUniqueId
import com.motorro.cookbook.model.Image
import com.motorro.cookbook.model.ListRecipe
import com.motorro.cookbook.model.RecipeCategory
import kotlin.uuid.Uuid

/**
 * Recipe list items.
 */
sealed class RecipeListItem : WithUniqueId, WithTypeId {
    /**
     * Recipe item.
     */
    data class RecipeItem(private val recipe: ListRecipe) : RecipeListItem() {
        override val uniqueId: Uuid get() = recipe.id

        val id: Uuid get() = recipe.id
        val title: String get() = recipe.title
        val imageUrl: Image? get() = recipe.image
    }

    /**
     * Category item.
     */
    data class CategoryItem(val category: RecipeCategory) : RecipeListItem() {
        /**
         * All categories have unique name as we distinct categories by name.
         */
        override val uniqueId: String = category.name

        val name: String get() = category.name
    }
}
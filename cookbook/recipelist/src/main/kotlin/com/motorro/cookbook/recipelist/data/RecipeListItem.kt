package com.motorro.cookbook.recipelist.data

import androidx.annotation.LayoutRes
import com.motorro.cookbook.appcore.ui.WithLayoutId
import com.motorro.cookbook.model.Image
import com.motorro.cookbook.model.ListRecipe
import com.motorro.cookbook.model.RecipeCategory
import com.motorro.cookbook.recipelist.R
import kotlin.uuid.Uuid

/**
 * Recipe list items.
 */
sealed class RecipeListItem : WithLayoutId {

    // See RecipeAdapter.kt
    abstract fun isSame(other: RecipeListItem): Boolean
    abstract fun isContentSame(other: RecipeListItem): Boolean

    /**
     * Recipe item.
     */
    data class RecipeItem(private val recipe: ListRecipe) : RecipeListItem(), WithLayoutId by RecipeItem {
        companion object : WithLayoutId {
            @get:LayoutRes
            override val layoutId: Int = R.layout.vh_recipe_item
        }

        val id: Uuid get() = recipe.id
        val title: String get() = recipe.title
        val imageUrl: Image? get() = recipe.image

        override fun isSame(other: RecipeListItem): Boolean = id == (other as? RecipeItem)?.id
        override fun isContentSame(other: RecipeListItem): Boolean {
            if (other !is RecipeItem) return false
            return title == other.title && imageUrl == other.imageUrl
        }
    }

    /**
     * Category item.
     */
    data class CategoryItem(val category: RecipeCategory) : RecipeListItem(), WithLayoutId by CategoryItem {
        companion object : WithLayoutId {
            @get:LayoutRes
            override val layoutId: Int = R.layout.vh_recipe_category
        }

        val name: String get() = category.name

        override fun isSame(other: RecipeListItem): Boolean = name == (other as? CategoryItem)?.name
        override fun isContentSame(other: RecipeListItem): Boolean = name == (other as? CategoryItem)?.name
    }
}
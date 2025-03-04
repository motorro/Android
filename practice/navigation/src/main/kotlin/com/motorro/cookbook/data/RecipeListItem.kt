package com.motorro.cookbook.data

import androidx.annotation.LayoutRes
import com.motorro.cookbook.R

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
    data class RecipeItem(private val recipe: Recipe) : RecipeListItem(), WithLayoutId by RecipeItem {
        companion object : WithLayoutId {
            @get:LayoutRes
            override val layoutId: Int = R.layout.vh_recipe_item
        }

        val id: Int get() = recipe.id
        val title: String get() = recipe.title
        val imageUrl: String? get() = recipe.imageUrl

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
package com.motorro.cookbook.app.data

import androidx.annotation.LayoutRes
import com.motorro.cookbook.app.R
import com.motorro.cookbook.data.Image
import com.motorro.cookbook.data.ListRecipe
import com.motorro.cookbook.data.RecipeCategory
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
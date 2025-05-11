package com.motorro.cookbook.app.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.motorro.cookbook.data.Image
import com.motorro.cookbook.data.ListRecipe
import com.motorro.cookbook.data.RecipeCategory
import com.motorro.cookbook.data.UserId
import kotlin.time.Instant
import kotlin.uuid.Uuid

/**
 * List recipe data
 */
@Entity(
    tableName = ListRecipeEntity.TABLE_NAME,
    primaryKeys = [ListRecipeEntity.COLUMN_USER_ID, ListRecipeEntity.COLUMN_RECIPE_ID]
)
data class ListRecipeEntity(
    @ColumnInfo(name = COLUMN_USER_ID)
    val userId: UserId,
    @ColumnInfo(name = COLUMN_RECIPE_ID)
    val recipeId: Uuid,
    val title: String,
    val categoryName: String,
    val imageUrl: String?,
    @ColumnInfo(index = true)
    val dateTimeCreated: Instant,
    @ColumnInfo(index = true)
    val deleted: Boolean = false
) {
    companion object {
        const val TABLE_NAME = "recipes"
        const val COLUMN_USER_ID = "userId"
        const val COLUMN_RECIPE_ID = "recipeId"
    }
}

/**
 * Converts recipe to entity
 */
fun ListRecipe.toEntity(userId: UserId) = ListRecipeEntity(
    userId = userId,
    recipeId = id,
    title = title,
    categoryName = category.name,
    imageUrl = image?.url,
    dateTimeCreated = dateTimeCreated,
    deleted = deleted
)

/**
 * Converts entity to list recipe
 */
fun ListRecipeEntity.toDomain() = ListRecipe(
    id = recipeId,
    title = title,
    category = RecipeCategory(categoryName),
    image = imageUrl?.let(::Image),
    dateTimeCreated = dateTimeCreated,
    deleted = deleted
)
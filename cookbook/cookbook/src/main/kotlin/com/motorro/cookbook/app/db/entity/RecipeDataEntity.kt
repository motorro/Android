package com.motorro.cookbook.app.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.motorro.cookbook.data.Image
import com.motorro.cookbook.data.Recipe
import com.motorro.cookbook.data.RecipeCategory
import com.motorro.cookbook.data.UserId
import kotlin.time.Instant
import kotlin.uuid.Uuid

/**
 * Extra recipe data
 */
@Entity(
    tableName = RecipeDataEntity.TABLE_NAME,
    primaryKeys = [ListRecipeEntity.COLUMN_USER_ID, ListRecipeEntity.COLUMN_RECIPE_ID],
    foreignKeys = [
        ForeignKey(
            entity = ListRecipeEntity::class,
            parentColumns = [ListRecipeEntity.COLUMN_USER_ID, ListRecipeEntity.COLUMN_RECIPE_ID],
            childColumns = [ListRecipeEntity.COLUMN_USER_ID, ListRecipeEntity.COLUMN_RECIPE_ID],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class RecipeDataEntity(
    @ColumnInfo(name = ListRecipeEntity.COLUMN_USER_ID)
    val userId: UserId,
    @ColumnInfo(name = ListRecipeEntity.COLUMN_RECIPE_ID)
    val recipeId: Uuid,
    val description: String
) {
    companion object {
        const val TABLE_NAME = "recipe_details"
    }
}

/**
 * Intermediate class to select full recipe
 */
data class RecipeData(
    @ColumnInfo(name = ListRecipeEntity.COLUMN_USER_ID)
    val userId: UserId,
    @ColumnInfo(name = ListRecipeEntity.COLUMN_RECIPE_ID)
    val recipeId: Uuid,
    val title: String,
    val categoryName: RecipeCategory,
    val imageUrl: String?,
    val description: String?,
    val dateTimeCreated: Instant,
    val deleted: Boolean
)

/**
 * Converts recipe to entity
 */
fun Recipe.toEntity(userId: UserId) = Pair(
    ListRecipeEntity(
        userId = userId,
        recipeId = id,
        title = title,
        categoryName = category.name,
        imageUrl = image?.url,
        dateTimeCreated = dateTimeCreated,
        deleted = deleted
    ),
    RecipeDataEntity(
        userId = userId,
        recipeId = id,
        description = description,
    )
)

/**
 * Converts entity to list recipe
 */
fun RecipeData.toDomain() = Recipe(
    id = recipeId,
    title = title,
    category = categoryName,
    image = imageUrl?.let(::Image),
    description = description.orEmpty(),
    dateTimeCreated = dateTimeCreated,
    deleted = deleted
)
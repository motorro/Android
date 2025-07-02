package com.motorro.cookbook.data.recipes.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.motorro.cookbook.data.recipes.db.entity.ListRecipeEntity
import com.motorro.cookbook.data.recipes.db.entity.RecipeData
import com.motorro.cookbook.data.recipes.db.entity.RecipeDataEntity
import com.motorro.cookbook.model.RecipeCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface CookbookDao {
    /**
     * Inserts recipe list
     * @param list List of recipes to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: List<ListRecipeEntity>)

    /**
     * Inserts recipe
     * @param recipe Main recipe data
     * @param details Recipe details
     */
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: ListRecipeEntity, details: RecipeDataEntity)

    /**
     * Returns a list of non-deleted recipes for the user
     * @param userId User ID
     */
    @Query("""
        SELECT * FROM recipes
        WHERE userId = :userId
        AND deleted = 0
        ORDER BY dateTimeCreated DESC
    """)
    fun list(userId: Int): Flow<List<ListRecipeEntity>>

    /**
     * Fetches recipe data
     * @param userId User ID
     * @param recipeId Recipe ID
     */
    @Query("""
        SELECT *, recipe_details.description AS description FROM recipes
        LEFT JOIN recipe_details USING (userId, recipeId)
        WHERE userId = :userId
        AND recipeId = :recipeId
    """)
    fun recipe(userId: Int, recipeId: String): Flow<List<RecipeData>>

    /**
     * Deletes recipe in user's cookbook by marking it deleted
     * @param userId User ID
     * @param recipeId Recipe ID
     */
    @Query("""
        UPDATE recipes
        SET deleted = 1
        WHERE userId = :userId
        AND recipeId = :recipeId
    """)
    suspend fun delete(userId: Int, recipeId: String)

    /**
     * Returns a list of distinct categories found in recipes for the user
     * @param userId User ID
     */
    @Query("""
        SELECT DISTINCT categoryName FROM recipes
        WHERE userId = :userId
        AND deleted = 0
        ORDER BY categoryName
    """)
    fun categories(userId: Int): Flow<List<RecipeCategory>>
}
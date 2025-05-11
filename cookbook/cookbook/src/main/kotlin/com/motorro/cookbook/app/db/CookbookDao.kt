package com.motorro.cookbook.app.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.motorro.cookbook.app.db.entity.ListRecipeEntity
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
}
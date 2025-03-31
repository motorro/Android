package com.motorro.sqlite.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.motorro.sqlite.data.Image
import com.motorro.sqlite.data.ListImage
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {
    @Transaction
    @Query("""
        SELECT * 
        FROM photo 
        ORDER BY created DESC
    """)
    fun getList(): Flow<List<ListImage>>

    @Transaction
    @Query("""
        SELECT * 
        FROM photo 
        WHERE 
            photo.name LIKE :nameSearch
        ORDER BY created DESC
    """)
    fun getList(nameSearch: String): Flow<List<ListImage>>

    @Transaction
    @Query("""
        SELECT * 
        FROM photo 
        WHERE 
            tag IN (:tags)
        ORDER BY created DESC
    """)
    fun getList(tags: Set<Int>): Flow<List<ListImage>>

    @Transaction
    @Query("""
        SELECT * 
        FROM photo 
        WHERE 
            photo.name LIKE :nameSearch
            AND tag IN (:tags)
        ORDER BY created DESC
    """)
    fun getList(nameSearch: String, tags: Set<Int>): Flow<List<ListImage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(image: Image)

    @Query("DELETE FROM photo WHERE path = :imagePath")
    suspend fun deleteImage(imagePath: String)
}
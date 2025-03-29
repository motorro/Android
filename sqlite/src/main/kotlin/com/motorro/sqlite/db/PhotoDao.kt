package com.motorro.sqlite.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.motorro.sqlite.data.Image
import com.motorro.sqlite.data.ListImage
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {
    @Query("SELECT path, name, created, tagname FROM photo ORDER BY created DESC")
    fun getList(): Flow<List<ListImage>>

    @Query("SELECT path, name, created, tagname FROM photo WHERE name LIKE :nameSearch ORDER BY created DESC")
    fun getList(nameSearch: String): Flow<List<ListImage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(image: Image)

    @Query("DELETE FROM photo WHERE path = :imagePath")
    suspend fun deleteImage(imagePath: String)
}
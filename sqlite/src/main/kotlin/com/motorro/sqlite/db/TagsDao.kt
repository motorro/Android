package com.motorro.sqlite.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.motorro.sqlite.data.ListTag
import com.motorro.sqlite.data.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface TagsDao {
    @Query("SELECT id, name FROM tag ORDER BY name")
    fun getList(): Flow<List<ListTag>>

    @Query("SELECT id, name FROM tag WHERE id IN (:ids) ORDER BY name")
    fun getList(ids: Array<Int>): Flow<List<ListTag>>

    @Query("SELECT * FROM tag WHERE id = :id")
    suspend fun getTag(id: Int): Tag?

    @Query("SELECT COUNT(*) FROM tag WHERE name = :nameSearch")
    suspend fun checkTagExists(nameSearch: String): Boolean

    @Upsert
    suspend fun upsertTag(tag: Tag)

    @Query("DELETE FROM tag WHERE id = :id")
    suspend fun deleteTag(id: Int)
}
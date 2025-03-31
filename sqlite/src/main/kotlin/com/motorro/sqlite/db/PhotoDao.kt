package com.motorro.sqlite.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.motorro.sqlite.data.Image
import com.motorro.sqlite.data.ListImageRelation
import com.motorro.sqlite.data.ListImageTag
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {
    @Transaction
    @Query("""
        SELECT * 
        FROM photo 
        ORDER BY created DESC
    """)
    fun getList(): Flow<List<ListImageRelation>>

    @Transaction
    @Query("""
        SELECT * 
        FROM photo 
        WHERE 
            photo.name LIKE :nameSearch
        ORDER BY created DESC
    """)
    fun getList(nameSearch: String): Flow<List<ListImageRelation>>

    @Query("""
        SELECT 
            photo.path path,
            photo.name name,
            photo.created created,
            tag.id tag_id,
            tag.name tag_name
        FROM photo
        LEFT JOIN pic_to_tag ON (photo.path = pic_to_tag.image_path)
        LEFT JOIN tag ON (pic_to_tag.tag_id = tag.id)
        WHERE 
            tag.id in (:tags)
        ORDER BY photo.created DESC
    """)
    fun getList(tags: Set<Int>): Flow<Map<Image, List<ListImageTag>>>

    @Query("""
        SELECT 
            photo.path path,
            photo.name name,
            photo.created created,
            tag.id tag_id,
            tag.name tag_name
        FROM photo
        LEFT JOIN pic_to_tag ON (photo.path = pic_to_tag.image_path)
        LEFT JOIN tag ON (pic_to_tag.tag_id = tag.id)
        WHERE 
            photo.name LIKE (:nameSearch)
            AND tag.id in (:tags)
        ORDER BY photo.created DESC
    """)
    fun getList(nameSearch: String, tags: Set<Int>): Flow<Map<Image, List<ListImageTag>>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(image: Image, picToTag: List<PicToTag>)

    @Query("DELETE FROM photo WHERE path = :imagePath")
    suspend fun deleteImage(imagePath: String)
}
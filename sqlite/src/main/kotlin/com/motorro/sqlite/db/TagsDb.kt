package com.motorro.sqlite.db

import com.motorro.sqlite.data.ListTag
import com.motorro.sqlite.data.Tag
import kotlinx.coroutines.flow.Flow

/**
 * Tags view model
 */
interface TagsDb {
    /**
     * Tags
     */
    fun getList(ids: Set<Int> = emptySet()): Flow<List<ListTag>>

    /**
     * Gets tag by id
     */
    suspend fun getTag(id: Int): Tag?

    /**
     * Checks if tag exists
     * @param nameSearch Tag name
     */
    suspend fun checkTagExists(nameSearch: String): Boolean

    /**
     * Adds or saves a tag
     */
    suspend fun upsertTag(tag: Tag)

    /**
     * Deletes a tag
     */
    suspend fun deleteTag(id: Int)
}
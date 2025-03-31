package com.motorro.sqlite.db

import android.net.Uri
import com.motorro.sqlite.data.Image
import com.motorro.sqlite.data.ListImage
import com.motorro.sqlite.data.PhotoFilter
import kotlinx.coroutines.flow.Flow

/**
 * Photo database
 */
interface PhotoDb {
    /**
     * Retrieves a list of photos
     * @param filter Filter
     */
    fun getList(filter: PhotoFilter): Flow<List<ListImage>>

    /**
     * Adds an image
     * @param image Image to add
     */
    suspend fun addImage(image: Image, tags: Set<Int>)

    /**
     * Deletes an image
     * @param imagePath Image path
     */
    suspend fun deleteImage(imagePath: Uri)

    /**
     * Tags database
     */
    val tagsDb: TagsDb
}
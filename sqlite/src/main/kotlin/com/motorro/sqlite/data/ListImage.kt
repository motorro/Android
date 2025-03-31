package com.motorro.sqlite.data

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.motorro.sqlite.db.PicToTag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime

/**
 * List image data
 */
interface ListImage {
    /**
     * Image path
     */
    val path: Uri

    /**
     * Image name
     */
    val name: String

    /**
     * Date and time image was taken
     */
    val dateTimeTaken: LocalDateTime

    /**
     * List of tags associated with the image
     */
    val tags: List<String>
}

/**
 * List image data
 * @param path Image path
 * @param name Image name
 * @param dateTimeTaken Date and time image was taken
 */
data class ListImageRelation(
    @Embedded
    private val imageData: Image,
    @Relation(
        parentColumn = Image.COLUMN_PATH,
        entityColumn = Tag.COLUMN_ID,
        associateBy = Junction(
            value = PicToTag::class,
            parentColumn = PicToTag.COLUMN_IMAGE_PATH,
            entityColumn = PicToTag.COLUMN_TAG_ID
        )
    )
    private val tagsData: List<Tag>
): ListImage {
    override val path: Uri get() = imageData.path
    override val name: String get() = imageData.name
    override val dateTimeTaken: LocalDateTime get() = imageData.dateTimeTaken
    override val tags: List<String> get() = tagsData.map { it.name }
}

/**
 * List image tag data
 * @param id Tag ID
 * @param name Tag name
 */
data class ListImageTag(
    @ColumnInfo(name = "tag_id")
    val id: Int,
    @ColumnInfo(name = "tag_name")
    val name: String,
)

/**
 * List image data for requests with multimap
 */
data class ListImageJoin(private val entry: Map.Entry<Image, List<ListImageTag>>): ListImage {
    override val path: Uri get() = entry.key.path
    override val name: String get() = entry.key.name
    override val dateTimeTaken: LocalDateTime get() = entry.key.dateTimeTaken
    override val tags: List<String> get() = entry.value.map { it.name }
}

/**
 * Converts a flow of list image relations to a flow of list images
 */
fun Flow<Map<Image, List<ListImageTag>>>.toListImages(): Flow<List<ListImage>> = map { map ->
    map.map { ListImageJoin(it) }
}

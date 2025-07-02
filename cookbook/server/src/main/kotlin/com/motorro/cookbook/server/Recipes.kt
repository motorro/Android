package com.motorro.cookbook.server

import com.motorro.cookbook.model.Image
import com.motorro.cookbook.model.ImageUpload
import com.motorro.cookbook.model.ListRecipe
import com.motorro.cookbook.model.Recipe
import com.motorro.cookbook.model.UserId
import com.motorro.cookbook.server.db.tables.RecipeEntity
import com.motorro.cookbook.server.db.tables.RecipesTable
import com.motorro.cookbook.server.db.tables.toListRecipe
import com.motorro.cookbook.server.db.tables.toRecipe
import io.ktor.http.ContentType
import io.ktor.http.Url
import io.ktor.server.plugins.NotFoundException
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.copyAndClose
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.core.dao.id.CompositeID
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.coroutines.CoroutineContext
import kotlin.time.Clock
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid

/**
 * Recipe ID
 */
data class RecipeId(val userId: UserId, val recipeId: Uuid)

interface Recipes {
    suspend fun getAll(userId: UserId): List<ListRecipe>
    suspend fun get(recipeId: RecipeId): Recipe
    suspend fun add(userId: UserId, recipe: Recipe): Recipe
    suspend fun setImage(recipeId: RecipeId, originalName: String?, contentType: ContentType?, fileStreamProvider: () -> ByteReadChannel): ImageUpload
    suspend fun delete(recipeId: RecipeId)

    class Impl(
        private val imagePath: Path,
        private val imageBase: Url,
        private val context: CoroutineContext = Dispatchers.IO,
        private val clock: Clock = Clock.System
    ) : Recipes {

        override suspend fun getAll(userId: UserId): List<ListRecipe> = tQuery {
            RecipeEntity.all().sortedByDescending { it.dateTimeCreated }.map { it.toListRecipe(imageBase) }
        }

        override suspend fun get(recipeId: RecipeId): Recipe = tQuery {
            RecipeEntity.findById(recipeId.toEntityId())?.toRecipe(imageBase) ?: throw NotFoundException("Recipe ${recipeId.recipeId} not found")
        }

        override suspend fun add(userId: UserId, recipe: Recipe): Recipe = tQuery {
            val id = CompositeID {
                it[RecipesTable.userId] = userId.id
                it[RecipesTable.recipeId] = recipe.id.toJavaUuid()
            }
            RecipeEntity.new(id) {
                title = recipe.title
                category = recipe.category
                image = recipe.image
                description = recipe.description
                dateTimeCreated = clock.now()
            }.toRecipe(imageBase)
        }

        override suspend fun setImage(
            recipeId: RecipeId,
            originalName: String?,
            contentType: ContentType?,
            fileStreamProvider: () -> ByteReadChannel
        ): ImageUpload {
            val fileName = originalName ?: "image.${contentType?.toString() ?: "jpg"}"
            val path = Paths.get("${recipeId.userId}", recipeId.recipeId.toString(), fileName)
            val file = imagePath.resolve(path).toFile()

            withContext(context) {
                file.toPath().parent.toFile().mkdirs()
                fileStreamProvider().copyAndClose(file.writeChannel(context))
            }

            val url = imageBase.ofRelative(path)

            val result = tQuery {
                RecipeEntity.findByIdAndUpdate(recipeId.toEntityId()) {
                    it.imagePath = path
                    it.image = null
                }
            }

            if (null == result) {
                withContext(context) {
                    file.delete()
                }
                throw NotFoundException("Recipe ${recipeId.recipeId} not found")
            }

            return ImageUpload(Image.create(url))
        }

        override suspend fun delete(recipeId: RecipeId) {
            val result = tQuery {
                RecipeEntity.findByIdAndUpdate(recipeId.toEntityId()) {
                    it.deleted = true
                }
            }

            if (null == result) {
                throw NotFoundException("Recipe ${recipeId.recipeId} not found")
            }
        }

        private fun RecipeId.toEntityId() = CompositeID {
            it[RecipesTable.userId] = userId.id
            it[RecipesTable.recipeId] = recipeId.toJavaUuid()
        }
    }
}

package com.motorro.cookbook.data.recipes.net

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.motorro.cookbook.core.error.toCore
import com.motorro.cookbook.data.net.request
import com.motorro.cookbook.data.recipes.CookbookApi
import com.motorro.cookbook.model.ImageUpload
import com.motorro.cookbook.model.ListRecipe
import com.motorro.cookbook.model.Recipe
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.forms.FormBuilder
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.appendEncodedPathSegments
import io.ktor.http.contentType
import io.ktor.utils.io.streams.asInput
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import java.net.URL
import javax.inject.Inject
import javax.inject.Named
import kotlin.uuid.Uuid


/**
 * Ktor cookbook API implementation
 */
class KtorCookbookApi @Inject constructor(
    @param:Named("Application") private val context: Context,
    private val httpClient: HttpClient,
    @param:Named("Cookbook") private val baseUrl: URL
) : CookbookApi {

    override suspend fun getRecipeList(): Result<List<ListRecipe>> = request {
        httpClient.get(baseUrl) {
            url { appendEncodedPathSegments("recipes") }
        }
    }

    override suspend fun getRecipe(id: Uuid): Result<Recipe> = request {
        httpClient.get(baseUrl) {
            url { appendEncodedPathSegments("recipes", id.toString()) }
        }
    }

    override suspend fun addRecipe(recipe: Recipe): Result<Recipe> = request {
        httpClient.post(baseUrl) {
            url { appendEncodedPathSegments("recipes") }
            contentType(ContentType.Application.Json)
            setBody(recipe)
        }
    }

    override suspend fun uploadRecipeImage(recipeId: Uuid, imageUri: Uri): Result<ImageUpload> = request {
        httpClient.put(baseUrl) {
            url { appendEncodedPathSegments("recipes", recipeId.toString(), "image") }
            setBody(
                MultiPartFormDataContent(
                    formData {
                        buildFileUploadPart("image", imageUri)
                    }
                )
            )
        }
    }

    override suspend fun deleteRecipe(recipeId: Uuid): Result<Unit> = try {
        httpClient.delete(baseUrl) {
            url { appendEncodedPathSegments("recipes", recipeId.toString()) }
        }
        Result.success(Unit)
    } catch (e: Throwable) {
        currentCoroutineContext().ensureActive()
        throw e.toCore()
    }

    /**
     * Builds file upload part
     * @param key Part key
     * @param fileUri File URI
     */
    private fun FormBuilder.buildFileUploadPart(key: String, fileUri: Uri) = with(context.contentResolver) {
        val contentType = requireNotNull(getType(fileUri)) {
            "Unable to get content type for $fileUri"
        }

        val (fileName, fileSize) = query(fileUri, null, null, null, null).use { cursor ->
            checkNotNull(cursor) {
                "Unable to get file name and size for $fileUri"
            }

            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

            cursor.moveToFirst()
            val fileName = cursor.getString(nameIndex) ?: "$key${Regex("/([\\w]+)").find(contentType)?.groups?.get(1)?.run { ".$value" }}"
            val fileSize = cursor.getLong(sizeIndex)

            fileName to fileSize
        }

        val inputStream = requireNotNull(openInputStream(fileUri)) {
            "Unable to open image stream for $fileUri"
        }

        appendInput(
            key = key,
            headers = Headers.build {
                append(HttpHeaders.ContentType, contentType)
                append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
            },
            size = fileSize,
            block = { inputStream.asInput() }
        )
    }
}
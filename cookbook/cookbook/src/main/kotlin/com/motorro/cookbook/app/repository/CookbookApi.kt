package com.motorro.cookbook.app.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.motorro.cookbook.app.net.request
import com.motorro.cookbook.data.ImageUpload
import com.motorro.cookbook.data.ListRecipe
import com.motorro.cookbook.data.Recipe
import io.ktor.client.HttpClient
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
import java.net.URL
import kotlin.uuid.Uuid

/**
 * Cookbook network API
 */
interface CookbookApi {
    /**
     * Load a list of recipes
     */
    suspend fun getRecipeList(): Result<List<ListRecipe>>

    /**
     * Load a recipe
     * @param id Recipe ID
     */
    suspend fun getRecipe(id: Uuid): Result<Recipe>

    /**
     * Adds recipe to cookbook
     * @param recipe Recipe to add
     */
    suspend fun addRecipe(recipe: Recipe): Result<Recipe>

    /**
     * Uploads recipe image
     * @param recipeId Recipe ID
     * @param imageUri Image URI
     */
    suspend fun uploadRecipeImage(recipeId: Uuid, imageUri: Uri): Result<ImageUpload>
}

/**
 * Ktor cookbook API implementation
 */
class KtorCookbookApi(
    private val context: Context,
    private val httpClient: HttpClient,
    private val baseUrl: URL
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
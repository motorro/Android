package com.motorro.cookbook.app.repository

import com.motorro.cookbook.app.net.request
import com.motorro.cookbook.data.ListRecipe
import com.motorro.cookbook.data.Recipe
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.appendEncodedPathSegments
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
}

/**
 * Ktor cookbook API implementation
 */
class KtorCookbookApi(
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

}
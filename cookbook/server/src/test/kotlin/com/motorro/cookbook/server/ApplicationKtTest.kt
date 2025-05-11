package com.motorro.cookbook.server

import com.motorro.cookbook.data.Image
import com.motorro.cookbook.data.ImageUpload
import com.motorro.cookbook.data.Profile
import com.motorro.cookbook.data.Recipe
import com.motorro.cookbook.data.UserId
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.basicAuth
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.basic
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.json.Json
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.uuid.Uuid

class ApplicationTest {

    private val json = Json

    private val badId = Uuid.parse("e42b81c7-b326-4f71-9df0-37f00e54102b")

    private val userId = 10
    private val username = "user"
    private val password = "password"
    
    private val user: User = mockk {
        every { this@mockk.auth(any(), any()) } answers {
            val authenticationConfig = firstArg<AuthenticationConfig>()
            val area = secondArg<String>()
            authenticationConfig.basic(area) {
                realm = "Test Server"
                validate { credentials ->
                    if (username == credentials.name && password == credentials.password) {
                        UserIdPrincipal(UserId(userId))
                    } else {
                        null
                    }
                }
            }
        }
        
        coEvery { this@mockk.getProfile(any()) } answers {
            val id = firstArg<Int>()
            if (id == userId) {
                Profile(UserId(userId), username)
            } else {
                throw NotFoundException("User with id $id not found")
            }
        }
    }

    private val uploadFile = Paths.get("src","test","resources","image.png").toFile().absoluteFile
    private val uploadResponse = ImageUpload(Image.create("https://example.com/picture.jpg"))

    private val recipes: Recipes = mockk {
        coEvery { this@mockk.getAll(any()) } returns listOf(listRecipe)
        coEvery { this@mockk.get(any()) } answers {
            val recipeId = firstArg<RecipeId>()
            if (recipeId.recipeId == recipe.id) {
                recipe
            } else {
                throw NotFoundException("User with id $recipeId not found")
            }
        }
        coEvery { this@mockk.add(any(), any()) } answers {
            secondArg<Recipe>()
        }
        coEvery { this@mockk.setImage(any(), anyNullable(), anyNullable(), any()) } answers {
            val recipeId = firstArg<RecipeId>()
            if (recipeId.recipeId == recipe.id) {
                ImageUpload(Image.create("https://example.com/picture.jpg"))
            } else {
                throw NotFoundException("Recipe with id $recipeId not found")
            }
        }
        coEvery { this@mockk.delete(any()) } answers {
            val recipeId = firstArg<RecipeId>()
            if (recipeId.recipeId == recipe.id) {
                Unit
            } else {
                throw NotFoundException("Recipe with id $recipeId not found")
            }
        }
    }

    private fun ApplicationTestBuilder.prepareClient(): HttpClient {
        return createClient {
            install(ContentNegotiation) {
                json(Json)
            }
        }
    }

    @Test
    fun profileResponds() = testApplication {
        application {
            module(recipes, user)
        }
        val response = prepareClient().get("/profile") {
            accept(ContentType.Application.Json)
            basicAuth(username, password)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(
            Profile(UserId(userId), username),
            json.decodeFromString(response.bodyAsText())
        )
    }

    @Test
    fun failsToProfileIfNotAuthenticated() = testApplication {
        application {
            module(recipes, user)
        }
        val response = prepareClient().get("/profile") {
            accept(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
        coVerify(exactly = 0) { user.getProfile(any()) }
    }

    @Test
    fun allResponds() = testApplication {
        application {
            module(recipes, user)
        }
        val response = prepareClient().get("/recipes") {
            accept(ContentType.Application.Json)
            basicAuth(username, password)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(
            listOf(listRecipe),
            json.decodeFromString(response.bodyAsText())
        )
    }

    @Test
    fun failsToAllIfNotAuthenticated() = testApplication {
        application {
            module(recipes, user)
        }
        val response = prepareClient().get("/recipes") {
            accept(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
        coVerify(exactly = 0) { recipes.getAll(any()) }
    }


    @Test
    fun recipeResponds() = testApplication {
        application {
            module(recipes, user)
        }
        val response = prepareClient().get("/recipes/${recipe.id}") {
            accept(ContentType.Application.Json)
            basicAuth(username, password)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(
            recipe,
            json.decodeFromString(response.bodyAsText())
        )
    }

    @Test
    fun failsOnRecipeNotFound() = testApplication {
        application {
            module(recipes, user)
        }
        val response = prepareClient().get("/recipes/${badId}") {
            accept(ContentType.Application.Json)
            basicAuth(username, password)
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun failsOnRecipeNotAuthorized() = testApplication {
        application {
            module(recipes, user)
        }
        val response = prepareClient().get("/recipes/${recipe.id}") {
            accept(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
        coVerify(exactly = 0) { recipes.get(any()) }
    }

    @Test
    fun addsRecipe() = testApplication {
        application {
            module(recipes, user)
        }
        val response = prepareClient().post("/recipes") {
            basicAuth(username, password)
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(recipe)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        coVerify { recipes.add(UserId(userId), recipe) }
        assertEquals(
            recipe,
            json.decodeFromString(response.bodyAsText())
        )
    }

    @Test
    fun failsToAddIfNotAuthenticated() = testApplication {
        application {
            module(recipes, user)
        }
        val response = prepareClient().post("/recipes") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(recipe)
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
        coVerify(exactly = 0) { recipes.add(any(),any()) }
    }

    @Test
    fun addsRecipeImage() = testApplication {
        application {
            module(recipes, user)
        }
        val response = prepareClient().put("/recipes/${recipe.id}/image") {
            basicAuth(username, password)
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("image", uploadFile.readBytes(), Headers.build {
                            append(HttpHeaders.ContentType, "image/png")
                            append(HttpHeaders.ContentDisposition, "filename=\"image.png\"")
                        })
                    }
                )
            )
        }

        assertEquals(HttpStatusCode.OK, response.status)
        coVerify { recipes.setImage(
            eq(RecipeId(UserId(userId), recipe.id)),
            eq("image.png"),
            eq(ContentType.Image.PNG),
            any()
        ) }
        assertEquals(
            uploadResponse,
            json.decodeFromString(response.bodyAsText())
        )
    }

    @Test
    fun failsToAddImageOnRecipeNotFound() = testApplication {
        application {
            module(recipes, user)
        }
        val response = prepareClient().put("/recipes/$badId/image") {
            basicAuth(username, password)
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("image", uploadFile.readBytes(), Headers.build {
                            append(HttpHeaders.ContentType, "image/png")
                            append(HttpHeaders.ContentDisposition, "filename=\"image.png\"")
                        })
                    }
                )
            )
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun failsToAddImageOnNotAuthenticated() = testApplication {
        application {
            module(recipes, user)
        }
        val response = prepareClient().put("/recipes/${recipe.id}/image") {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("image", uploadFile.readBytes(), Headers.build {
                            append(HttpHeaders.ContentType, "image/png")
                            append(HttpHeaders.ContentDisposition, "filename=\"image.png\"")
                        })
                    }
                )
            )
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
        coVerify(exactly = 0) { recipes.delete(any()) }
    }

    @Test
    fun deletesRecipe() = testApplication {
        application {
            module(recipes, user)
        }
        val response = prepareClient().delete("/recipes/${recipe.id}") {
            basicAuth(username, password)
            accept(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.NoContent, response.status)
        coVerify { recipes.delete(RecipeId(UserId(userId), recipe.id)) }
    }

    @Test
    fun failsToDeleteOnRecipeNotFound() = testApplication {
        application {
            module(recipes, user)
        }
        val response = prepareClient().delete("/recipes/$badId") {
            basicAuth(username, password)
            accept(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun failsToDeleteOnNotAuthenticated() = testApplication {
        application {
            module(recipes, user)
        }
        val response = prepareClient().delete("/recipes/${recipe.id}") {
            accept(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
        coVerify(exactly = 0) { recipes.delete(any()) }
    }
}
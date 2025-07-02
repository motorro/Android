package com.motorro.cookbook.server

import com.motorro.cookbook.model.Image
import com.motorro.cookbook.model.Recipe
import com.motorro.cookbook.model.RecipeCategory
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments
import java.nio.file.Paths
import kotlin.time.Clock
import kotlin.uuid.Uuid

val SERVER_PROTOCOL = URLProtocol.HTTP
const val SERVER_HOST = "0.0.0.0"
const val EXTERNAL_SERVER_HOST = "10.0.2.2"
const val SERVER_PORT = 8080
internal const val USER = "user"
internal const val PASSWORD = "password"
internal val BASE_URL = URLBuilder(
    protocol = SERVER_PROTOCOL,
    host = EXTERNAL_SERVER_HOST,
    port = SERVER_PORT
).build()

internal val DATA_PATH = Paths.get("data").toAbsolutePath()
internal val DB_PATH = DATA_PATH.resolve("recipes.db")
internal val IMAGE_PATH = DATA_PATH.resolve("images")
internal val IMAGE_BASE = URLBuilder(BASE_URL).appendPathSegments("images").build()

internal const val DELAY = 2000L

internal val sampleRecipes = listOf(
    Recipe(
        id = Uuid.random(),
        title = "Sample recipe",
        category = RecipeCategory("Samples"),
        description = "Description for sample recipe",
        image = Image("https://picsum.photos/id/22/200/200"),
        dateTimeCreated = Clock.System.now()
    )
)
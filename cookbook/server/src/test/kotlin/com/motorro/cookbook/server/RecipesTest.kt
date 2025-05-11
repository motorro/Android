package com.motorro.cookbook.server

import com.motorro.cookbook.data.Image
import com.motorro.cookbook.data.Recipe
import com.motorro.cookbook.data.RecipeCategory
import com.motorro.cookbook.data.UserId
import com.motorro.cookbook.server.db.tables.RecipeEntity
import com.motorro.cookbook.server.db.tables.RecipesTable
import io.ktor.http.ContentType
import io.ktor.http.Url
import io.ktor.server.plugins.NotFoundException
import io.ktor.util.cio.readChannel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.v1.core.dao.id.CompositeID
import org.junit.Before
import org.junit.Test
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createTempDirectory
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid

class RecipesTest {
    private val now: Instant = Instant.parse("2025-05-04T09:15:00Z")
    private val imageBase = Url("http://example.com/images")

    private lateinit var imagePath: Path
    private lateinit var clock: Clock
    private lateinit var recipes: Recipes

    @Before
    fun init() = runBlocking {
        imagePath = createTempDirectory("io-test")
        clock = mockk {
            every { now() } returns now
        }

        recipes = Recipes.Impl(
            imagePath,
            imageBase,
            Dispatchers.Unconfined,
            clock
        )
    }

    private fun createRecipe(userId: UserId, block: RecipeEntity.() -> Unit = { }): Uuid = tQuery {
        val id = Uuid.random()
        val recipeId = CompositeID {
            it[RecipesTable.userId] = userId.id
            it[RecipesTable.recipeId] = id.toJavaUuid()
        }
        RecipeEntity.new(recipeId) {
            title = "Recipe"
            category = RecipeCategory("Category")
            image = Image.create("http://example.com/images/$id/image.jpg")
            description = "Recipe description"
            dateTimeCreated = Instant.parse("2025-05-04T08:15:00Z")
            block()
        }

        return@tQuery id
    }

    @Test
    fun returnsAllRecipes() = dbTest { userId ->
        val id = createRecipe(userId)
        val list = recipes.getAll(userId)
        assertEquals(1, list.size)
        list.first().let {
            assertEquals(id, it.id)
            assertEquals(
                Image.create("http://example.com/images/$id/image.jpg"),
                it.image
            )
        }
    }

    @Test
    fun returnsRecipeById() = dbTest { userId ->
        val id = createRecipe(userId)
        val recipe = recipes.get(RecipeId(userId, id))
        assertEquals(id, recipe.id)
        assertEquals(
            Image.create("http://example.com/images/$id/image.jpg"),
            recipe.image
        )
    }

    @Test
    fun notFoundIfRecipeNotFound() = dbTest { userId ->
        assertFailsWith<NotFoundException> {
            recipes.get(RecipeId(userId, Uuid.random()))
        }
    }

    @Test
    fun addsRecipe() = dbTest { userId ->
        val id = Uuid.random()
        val recipe = recipes.add(userId, Recipe(
            id = id,
            title = "Recipe",
            category = RecipeCategory("Category"),
            image = Image.create("http://example.com/images/123/image.jpg"),
            description = "Recipe description",
            dateTimeCreated = now.minus(1.days)
        )
        )

        assertEquals(id, recipe.id)
        assertEquals("Recipe", recipe.title)
        assertEquals(RecipeCategory("Category"), recipe.category)
        assertEquals("Recipe description", recipe.description)
        assertEquals(
            Image.create("http://example.com/images/123/image.jpg"),
            recipe.image
        )
        assertEquals(now, recipe.dateTimeCreated)
        assertFalse { recipe.deleted }

        val fromDb = assertNotNull(RecipeEntity.findById(CompositeID {
            it[RecipesTable.userId] = userId.id
            it[RecipesTable.recipeId] = id.toJavaUuid()
        }))

        assertEquals(id.toJavaUuid(), fromDb.recipeId.value)
        assertEquals("Recipe", fromDb.title)
        assertEquals(RecipeCategory("Category"), fromDb.category)
        assertEquals("Recipe description", fromDb.description)
        assertEquals(
            Image.create("http://example.com/images/123/image.jpg"),
            fromDb.image
        )
        assertFalse { fromDb.deleted }
    }

    @Test
    fun updatesImage() = dbTest { userId ->
        val id = createRecipe(userId)

        val result = recipes.setImage(RecipeId(userId, id), "image.png", ContentType.Image.PNG) {
            Paths.get("src", "test", "resources", "image.png").readChannel()
        }

        assertEquals(
            Image.create("http://example.com/images/$userId/$id/image.png"),
            result.image
        )

        val fromDb = assertNotNull(RecipeEntity.findById(CompositeID {
            it[RecipesTable.userId] = userId.id
            it[RecipesTable.recipeId] = id.toJavaUuid()
        }))

        assertEquals(Paths.get("$userId/$id/image.png"), fromDb.imagePath)
        assertNull(fromDb.image)

        val path = assertNotNull(fromDb.imagePath)
        assertEquals(Paths.get("$userId/$id/image.png"), path)
        assertTrue { imagePath.resolve(path).toFile().exists() }
        assertTrue { imagePath.resolve(path).toFile().isFile() }
    }

    @Test
    fun deletesRecipe() = dbTest { userId ->
        val id = createRecipe(userId)
        recipes.delete(RecipeId(userId, id))

        val fromDb = assertNotNull(RecipeEntity.findById(CompositeID {
            it[RecipesTable.userId] = userId.id
            it[RecipesTable.recipeId] = id.toJavaUuid()
        }))
        assertTrue { fromDb.deleted }
    }

    @Test
    fun notFoundIfRecipeNotFoundOnDelete() = dbTest { userId ->
        assertFailsWith<NotFoundException> {
            recipes.delete(RecipeId(userId, Uuid.random()))
        }
    }
}
package com.motorro.cookbook.server.db.tables

import com.motorro.cookbook.model.Image
import com.motorro.cookbook.model.ListRecipe
import com.motorro.cookbook.model.Recipe
import com.motorro.cookbook.model.RecipeCategory
import com.motorro.cookbook.server.create
import com.motorro.cookbook.server.ofRelative
import io.ktor.http.Url
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.CompositeID
import org.jetbrains.exposed.v1.core.dao.id.CompositeIdTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.CompositeEntity
import org.jetbrains.exposed.v1.dao.CompositeEntityClass
import org.jetbrains.exposed.v1.datetime.timestamp
import java.nio.file.Path
import java.nio.file.Paths
import java.util.UUID
import kotlin.uuid.toKotlinUuid

const val RECIPES_TABLE_NAME = "recipes"
const val MAX_RECIPE_TITLE = 512
const val MAX_RECIPE_CATEGORY = 128
const val MAX_URL = 2048

/**
 * Recipes for user
 */
object RecipesTable : CompositeIdTable(RECIPES_TABLE_NAME) {
    val userId: Column<EntityID<Int>> = reference(USERS_ID, UsersTable, onUpdate = ReferenceOption.CASCADE, onDelete = ReferenceOption.CASCADE)
    val recipeId: Column<EntityID<UUID>> = uuid("recipe_id").entityId()
    val title = varchar("title", MAX_RECIPE_TITLE)
    val category = varchar("category", MAX_RECIPE_CATEGORY)
    val imagePath = varchar("image_path", MAX_URL).nullable()
    val image = varchar("image_url", MAX_URL).nullable()
    val description = text("description")
    val dateTimeCreated = timestamp("created")
    val deleted = bool("deleted").default(false).index()

    override val primaryKey = PrimaryKey(userId, recipeId)

    init {
        addIdColumn(userId)
    }
}

/**
 * Recipe entity
 */
class RecipeEntity(id: EntityID<CompositeID>) : CompositeEntity(id) {
    var recipeId: EntityID<UUID> by RecipesTable.recipeId
    var title by RecipesTable.title
    var category: RecipeCategory by RecipesTable.category.transform({ it.name }, { RecipeCategory(it) })
    var imagePath: Path? by RecipesTable.imagePath.transform({ path -> path?.toString() }, { str -> str?.let(Paths::get) })
    var image: Image? by RecipesTable.image.transform({ url -> url?.toString() }, { str -> str?.let { Image.create(str) } })
    var description by RecipesTable.description
    var dateTimeCreated by RecipesTable.dateTimeCreated
    var deleted by RecipesTable.deleted

    companion object : CompositeEntityClass<RecipeEntity>(RecipesTable)
}

fun RecipeEntity.toListRecipe(imageBase: Url) = ListRecipe(
    id = recipeId.value.toKotlinUuid(),
    title = title,
    category = category,
    image = image(imageBase),
    dateTimeCreated = dateTimeCreated,
    deleted = deleted
)

fun RecipeEntity.toRecipe(imageBase: Url) = Recipe(
    id = recipeId.value.toKotlinUuid(),
    title = title,
    category = category,
    image = image(imageBase),
    description = description,
    dateTimeCreated = dateTimeCreated,
    deleted = deleted
)

private fun RecipeEntity.image(imageBase: Url): Image? {
    val image = image
    val path = imagePath
    return when {
        null != image -> image
        null != path -> Image.create(imageBase.ofRelative(path))
        else -> null
    }
}
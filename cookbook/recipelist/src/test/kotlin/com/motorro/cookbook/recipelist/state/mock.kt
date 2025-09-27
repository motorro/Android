package com.motorro.cookbook.recipelist.state

import com.motorro.cookbook.core.error.UnknownException
import com.motorro.cookbook.model.Image
import com.motorro.cookbook.model.ListRecipe
import com.motorro.cookbook.model.RecipeCategory
import java.io.IOException
import kotlin.time.Instant
import kotlin.uuid.Uuid

internal val ID = Uuid.random()

internal val LIST_RECIPE: ListRecipe = ListRecipe(
    id = ID,
    title = "Recipe 1",
    category = RecipeCategory("Category 1"),
    image = Image("https://picsum.photos/id/22/200/200"),
    dateTimeCreated = Instant.parse("2024-10-01T00:00:00Z"),
)

internal val ERROR_NON_FATAL = UnknownException(IOException("Non-fatal"), false)
internal val ERROR_FATAL = UnknownException(IOException("Fatal"), true)

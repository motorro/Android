package com.motorro.cookbook.recipe.state

import com.motorro.cookbook.core.error.UnknownException
import com.motorro.cookbook.domain.session.error.UnauthorizedException
import com.motorro.cookbook.model.Image
import com.motorro.cookbook.model.Recipe
import com.motorro.cookbook.model.RecipeCategory
import java.io.IOException
import kotlin.time.Instant
import kotlin.uuid.Uuid

internal val ID = Uuid.random()

internal val RECIPE: Recipe = Recipe(
    ID,
    "Recipe 1",
    RecipeCategory("Category 1"),
    Image("https://picsum.photos/id/22/200/200"),
    "Description 1",
    Instant.parse("2024-10-01T00:00:00Z"),
)

internal val ERROR_NO_NFATAL = UnknownException(IOException("Non-fatal"), false)
internal val ERROR_FATAL = UnknownException(IOException("Fatal"), true)
internal val ERROR_AUTH = UnauthorizedException()

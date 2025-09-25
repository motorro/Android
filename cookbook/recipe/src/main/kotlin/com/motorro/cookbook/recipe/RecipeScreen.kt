package com.motorro.cookbook.recipe

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.motorro.cookbook.appcore.compose.ui.auth.AuthPromptView
import com.motorro.cookbook.appcore.compose.ui.lce.LceView
import com.motorro.cookbook.appcore.compose.ui.loading.LoadingView
import com.motorro.cookbook.appcore.compose.ui.theme.AppDimens
import com.motorro.cookbook.appcore.compose.ui.theme.CookbookTheme
import com.motorro.cookbook.appcore.compose.ui.theme.appBarColors
import com.motorro.cookbook.core.error.UnknownException
import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.domain.session.error.UnauthorizedException
import com.motorro.cookbook.model.Image
import com.motorro.cookbook.model.Recipe
import com.motorro.cookbook.model.RecipeCategory
import com.motorro.cookbook.recipe.data.RecipeGesture
import com.motorro.cookbook.recipe.data.RecipeViewState
import java.io.IOException
import kotlin.time.Instant
import kotlin.uuid.Uuid
import com.motorro.cookbook.appcore.R as ACR

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RecipeScreen(
    viewState: RecipeViewState,
    onGesture: (RecipeGesture) -> Unit,
    onTerminated: () -> Unit,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val recipeTitle = when (viewState) {
        is RecipeViewState.Content -> viewState.state.data?.title
        is RecipeViewState.DeleteConfirmation -> viewState.data.title
        else -> null
    } ?: stringResource(R.string.title_recipe)

    if (viewState is RecipeViewState.DeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { onGesture(RecipeGesture.CancelDelete) },
            title = { Text(stringResource(R.string.title_delete)) },
            text = { Text(stringResource(R.string.text_delete, recipeTitle)) },
            confirmButton = {
                Button(
                    onClick = { onGesture(RecipeGesture.ConfirmDelete) }
                ) {
                    Text(stringResource(ACR.string.btn_ok))
                }
            },
            dismissButton = {
                Button(
                    onClick = { onGesture(RecipeGesture.CancelDelete) }
                ) {
                    Text(stringResource(ACR.string.btn_cancel))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = recipeTitle)
                },
                navigationIcon = {
                    IconButton(onClick = { onGesture(RecipeGesture.Back) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(ACR.string.btn_back)
                        )
                    }
                },
                actions = {
                    val canDelete = viewState is RecipeViewState.Content && viewState.deleteEnabled
                    if (canDelete) {
                        IconButton(onClick = { onGesture(RecipeGesture.Delete) }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = stringResource(R.string.delete),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                },
                colors = MaterialTheme.appBarColors()
            )
        }
    ) { paddingValues ->
        when(viewState) {
            is RecipeViewState.Content -> {
                LceView(
                    state = viewState.state,
                    onErrorAction = { onGesture(RecipeGesture.DismissError) },
                    modifier = modifier.padding(paddingValues),
                    loading = { d, m ->
                        if (null == d) {
                            LoadingView(m)
                        } else {
                            Box(modifier = m.fillMaxWidth()) {
                                LinearProgressIndicator(modifier = Modifier.fillMaxWidth().zIndex(2f).align(Alignment.TopCenter))
                                RecipeContent(d, modifier.zIndex(1f).align(Alignment.TopCenter))
                            }
                        }

                    },
                    onLogin = { m ->
                        AuthPromptView(onLogin, m)
                    },
                    content = { d, _, m ->
                        RecipeContent(d, m)
                    }
                )
            }
            is RecipeViewState.DeleteConfirmation -> {
                RecipeContent(viewState.data, modifier.padding(paddingValues))
            }
            RecipeViewState.Terminated -> LaunchedEffect(viewState) {
                onTerminated()
            }
        }
    }
}

@Composable
@OptIn(ExperimentalGlideComposeApi::class)
fun RecipeContent(recipe: Recipe, modifier: Modifier = Modifier, loading: Boolean = false) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(bottom = AppDimens.vertical_margin)
    ) {
        Text(
            text = recipe.title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth().padding(AppDimens.horizontal_margin)
        )

        Spacer(modifier = Modifier.height(AppDimens.spacer_small))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .padding(
                    top = AppDimens.vertical_margin_small,
                    start = AppDimens.horizontal_margin,
                    end = AppDimens.horizontal_margin
                )
                .clip(MaterialTheme.shapes.medium)
                .border(1.dp, Color.Gray, MaterialTheme.shapes.medium),
            contentAlignment = Alignment.Center
        ) {
            if (null != recipe.image) {
                GlideImage(
                    model = recipe.image?.url,
                    contentDescription = stringResource(R.string.desc_the_image_of_the_recipe),
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    painter = painterResource(ACR.drawable.ic_image),
                    contentDescription = stringResource(R.string.desc_the_image_of_the_recipe),
                    modifier = Modifier.fillMaxSize(0.5f),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(AppDimens.spacer_small))

        Text(
            text = recipe.category.name,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.fillMaxWidth().padding(AppDimens.horizontal_margin)
        )

        Spacer(modifier = Modifier.height(AppDimens.spacer_small))

        Text(
            text = recipe.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth().padding(AppDimens.horizontal_margin)
        )
    }
}


// --- Preview ---
@Composable
@Preview(showBackground = true, name = "Recipe Content Preview")
fun RecipeContentPreview() {
    CookbookTheme {
        RecipeContent(
            recipe = Recipe(
                id = Uuid.parse("123e4567-e89b-12d3-a456-426614174000"),
                title = "Test recipe",
                category = RecipeCategory("Test category"),
                image = Image.create("https://picsum.photos/id/22/200/200"),
                description = "Test recipe description",
                dateTimeCreated = Instant.parse("2025-05-03T06:57:00Z")
            )
        )
    }
}

@Composable
@Preview(showBackground = true, name = "Recipe Screen - Loading")
fun RecipeScreenLoadingPreview() {
    CookbookTheme {
        RecipeScreen(
            viewState = RecipeViewState.Content(LceState.Loading(null), deleteEnabled = false),
            onGesture = {},
            onTerminated = {},
            onLogin = {}
        )
    }
}

@Composable
@Preview(showBackground = true, name = "Recipe Screen - Content")
fun RecipeScreenContentPreview() {
    CookbookTheme {
        RecipeScreen(
            viewState = RecipeViewState.Content(
                LceState.Content(
                    data = Recipe(
                        id = Uuid.parse("123e4567-e89b-12d3-a456-426614174000"),
                        title = "Test recipe",
                        category = RecipeCategory("Test category"),
                        image = Image.create("https://picsum.photos/id/22/200/200"),
                        description = "Test recipe description",
                        dateTimeCreated = Instant.parse("2025-05-03T06:57:00Z")
                    )
                ),
                deleteEnabled = true
            ),
            onGesture = {},
            onTerminated = {},
            onLogin = {}
        )
    }
}

@Preview(showBackground = true, name = "Recipe Screen - Error")
@Composable
fun RecipeScreenErrorPreview() {
    CookbookTheme {
        RecipeScreen(
            viewState = RecipeViewState.Content(LceState.Error(UnknownException(IOException("Preview error"))), deleteEnabled = false),
            onGesture = {},
            onTerminated = {},
            onLogin = {}
        )
    }
}

@Composable
@Preview(showBackground = true, name = "Recipe Screen - Unauthorized")
fun RecipeScreenUnauthorizedPreview() {
    CookbookTheme {
        RecipeScreen(
            viewState = RecipeViewState.Content(LceState.Error(UnauthorizedException()), deleteEnabled = false),
            onGesture = {},
            onTerminated = {},
            onLogin = {}
        )
    }
}

@Composable
@Preview(showBackground = true, name = "Recipe Screen - Delete Confirmation")
fun RecipeScreenDeleteConfirmPreview() {
    CookbookTheme {
        RecipeScreen(
            viewState = RecipeViewState.DeleteConfirmation(
                data = Recipe(
                    id = Uuid.parse("123e4567-e89b-12d3-a456-426614174000"),
                    title = "Test recipe",
                    category = RecipeCategory("Test category"),
                    image = Image.create("https://picsum.photos/id/22/200/200"),
                    description = "Test recipe description",
                    dateTimeCreated = Instant.parse("2025-05-03T06:57:00Z")
                )
            ),
            onGesture = {},
            onTerminated = {},
            onLogin = {}
        )
    }
}
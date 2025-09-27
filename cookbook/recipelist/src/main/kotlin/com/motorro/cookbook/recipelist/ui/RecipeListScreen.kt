package com.motorro.cookbook.recipelist.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.motorro.cookbook.appcore.compose.ui.auth.AuthPromptView
import com.motorro.cookbook.appcore.compose.ui.lce.LceView
import com.motorro.cookbook.appcore.compose.ui.loading.LoadingScreen
import com.motorro.cookbook.appcore.compose.ui.loading.LoadingView
import com.motorro.cookbook.appcore.compose.ui.theme.AppDimens
import com.motorro.cookbook.appcore.compose.ui.theme.CookbookTheme
import com.motorro.cookbook.appcore.compose.ui.theme.appBarColors
import com.motorro.cookbook.core.error.UnknownException
import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.model.ListRecipe
import com.motorro.cookbook.model.RecipeCategory
import com.motorro.cookbook.recipelist.R
import com.motorro.cookbook.recipelist.data.RecipeListGesture
import com.motorro.cookbook.recipelist.data.RecipeListItem
import com.motorro.cookbook.recipelist.data.RecipeListViewState
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Composable
fun RecipeListScreen(
    viewState: RecipeListViewState,
    onGesture: (RecipeListGesture) -> Unit,
    onRecipe: (Uuid) -> Unit,
    onAddRecipe: () -> Unit,
    onLogin: () -> Unit,
    onTerminated: () -> Unit,
    modifier: Modifier = Modifier
) {
  when(viewState) {
      RecipeListViewState.Loading -> LoadingScreen(
          title = stringResource(id = R.string.title_recipe_list),
          modifier = modifier
      )
      is RecipeListViewState.Content -> RecipeListScreen(
          viewState = viewState,
          onGesture = onGesture,
          onRecipe = onRecipe,
          onAddRecipe = onAddRecipe,
          onLogin = onLogin,
          modifier = modifier
      )
      RecipeListViewState.Terminated -> LaunchedEffect(viewState) {
          onTerminated()
      }
  }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RecipeListScreen(
    viewState: RecipeListViewState.Content,
    onGesture: (RecipeListGesture) -> Unit,
    onRecipe: (Uuid) -> Unit,
    onAddRecipe: () -> Unit,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.title_recipe_list)) },
                actions = {
                    IconButton(onClick = { onGesture(RecipeListGesture.Refresh) }, enabled = viewState.refreshEnabled) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = stringResource(id = R.string.btn_sync)
                        )
                    }
                    IconButton(onClick = { onGesture(RecipeListGesture.Logout) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ExitToApp,
                            contentDescription = stringResource(id = R.string.btn_logout)
                        )
                    }
                },
                colors = MaterialTheme.appBarColors()
            )
        },
        floatingActionButton = {
            AnimatedVisibility(viewState.addEnabled) {
                FloatingActionButton(
                    onClick = onAddRecipe,
                    containerColor = MaterialTheme.colorScheme.primary,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.content_add_recipe)
                    )
                }
            }
        }
    ) { paddingValues ->
        LceView(
            state = viewState.state,
            onErrorAction = { onGesture(RecipeListGesture.DismissError) },
            modifier = modifier.padding(paddingValues),
            loading = { d, m ->
                if (null == d) {
                    LoadingView(m)
                } else {
                    Box(modifier = m.fillMaxWidth()) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter).zIndex(2f))
                        RecipeListContent(d, onRecipe, Modifier.align(Alignment.TopCenter).zIndex(1f))
                    }
                }

            },
            onLogin = { m ->
                AuthPromptView(onLogin, m)
            },
            content = { d, _, m ->
                RecipeListContent(d, onRecipe, m)
            }
        )
    }
}

@Composable
private fun RecipeListContent(items: List<RecipeListItem>, onRecipe: (Uuid) -> Unit, modifier: Modifier = Modifier) {
    WithLocalGlide {
        LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(AppDimens.vertical_margin_small)) {
            itemsIndexed(items = items, key = { _, it -> it.uniqueId }, contentType = { _, it -> it.typeId }) { index, item ->
                when (item) {
                    is RecipeListItem.RecipeItem -> {
                        RecipeItemView(
                            recipeItem = item,
                            modifier = Modifier.clickable { onRecipe(item.id) }
                        )
                    }
                    is RecipeListItem.CategoryItem -> CategoryItemView(categoryItem = item)
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, name = "Recipe List Screen - Loading")
fun PreviewRecipeListScreenLoading() {
    CookbookTheme {
        RecipeListScreen(
            viewState = RecipeListViewState.Content(LceState.Loading(null), addEnabled = false, refreshEnabled = false),
            onGesture = {},
            onRecipe = {},
            onAddRecipe = {},
            onLogin = {},
            onTerminated = {}
        )
    }
}

@Composable
@Preview(showBackground = true, name = "Recipe List Screen - Content")
fun PreviewRecipeListScreenContent() {
    val sampleRecipes = listOf(
        RecipeListItem.CategoryItem(RecipeCategory("Appetizers")),
        RecipeListItem.RecipeItem(
            ListRecipe(
                id = Uuid.random(),
                title = "Bruschetta",
                category = RecipeCategory("Appetizers"),
                image = null,
                dateTimeCreated = Instant.parse("2023-01-01T10:00:00Z")
            )
        ),
        RecipeListItem.CategoryItem(RecipeCategory("Main Courses")),
        RecipeListItem.RecipeItem(
            ListRecipe(
                id = Uuid.random(),
                title = "https://picsum.photos/id/23/200/200",
                category = RecipeCategory("Main Courses"),
                image = null,
                dateTimeCreated = Instant.parse("2023-01-01T12:00:00Z")
            )
        ),
        RecipeListItem.RecipeItem(
            ListRecipe(
                id = Uuid.random(),
                title = "Grilled Salmon",
                category = RecipeCategory("Main Courses"),
                image = null,
                dateTimeCreated = Instant.parse("2023-01-01T12:30:00Z")
            )
        )
    )
    CookbookTheme {
        RecipeListScreen(
            viewState = RecipeListViewState.Content(LceState.Content(sampleRecipes), addEnabled = true, refreshEnabled = true),
            onGesture = {},
            onRecipe = {},
            onAddRecipe = {},
            onLogin = {},
            onTerminated = {}
        )
    }
}

@Composable
@Preview(showBackground = true, name = "Recipe List Screen - Content - Loading")
fun PreviewRecipeListScreenContentLoading() {
    val sampleRecipes = listOf(
        RecipeListItem.CategoryItem(RecipeCategory("Appetizers")),
        RecipeListItem.RecipeItem(
            ListRecipe(
                id = Uuid.random(),
                title = "Bruschetta",
                category = RecipeCategory("Appetizers"),
                image = null,
                dateTimeCreated = Instant.parse("2023-01-01T10:00:00Z")
            )
        ),
        RecipeListItem.CategoryItem(RecipeCategory("Main Courses")),
        RecipeListItem.RecipeItem(
            ListRecipe(
                id = Uuid.random(),
                title = "https://picsum.photos/id/23/200/200",
                category = RecipeCategory("Main Courses"),
                image = null,
                dateTimeCreated = Instant.parse("2023-01-01T12:00:00Z")
            )
        ),
        RecipeListItem.RecipeItem(
            ListRecipe(
                id = Uuid.random(),
                title = "Grilled Salmon",
                category = RecipeCategory("Main Courses"),
                image = null,
                dateTimeCreated = Instant.parse("2023-01-01T12:30:00Z")
            )
        )
    )
    CookbookTheme {
        RecipeListScreen(
            viewState = RecipeListViewState.Content(LceState.Content(sampleRecipes), addEnabled = true, refreshEnabled = true),
            onGesture = {},
            onRecipe = {},
            onAddRecipe = {},
            onLogin = {},
            onTerminated = {}
        )
    }
}

@Composable
@Preview(showBackground = true, name = "Recipe List Screen - Error")
fun PreviewRecipeListScreenError() {
    CookbookTheme {
        RecipeListScreen(
            viewState = RecipeListViewState.Content(
                LceState.Error(UnknownException(RuntimeException("Failed to load recipes")), null),
                addEnabled = false,
                refreshEnabled = false
            ),
            onGesture = {},
            onRecipe = {},
            onAddRecipe = {},
            onLogin = {},
            onTerminated = {}
        )
    }
}
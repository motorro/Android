package com.motorro.cookbook.app

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.motorro.cookbook.app.data.CookbookGesture
import com.motorro.cookbook.app.data.CookbookViewState
import com.motorro.cookbook.appcore.compose.ui.loading.LoadingScreen
import com.motorro.cookbook.recipelist.ui.RecipeListScreen

@Composable
fun MainScreen(state: CookbookViewState, onGesture: (CookbookGesture) -> Unit, onTerminate: () -> Unit) {

    BackHandler { onGesture(CookbookGesture.Back) }

    when (state) {
        CookbookViewState.Loading -> LoadingScreen(stringResource(R.string.app_name))

        is CookbookViewState.RecipeListFlow -> RecipeListScreen(
            viewState = state.child,
            onGesture = { onGesture(CookbookGesture.RecipeListFlow(it)) }
        )

        CookbookViewState.Terminated -> LaunchedEffect(state) {
            onTerminate()
        }
    }
}
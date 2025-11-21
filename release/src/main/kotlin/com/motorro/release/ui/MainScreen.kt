package com.motorro.release.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.motorro.composecore.ui.LoadingScreen
import com.motorro.release.api.LocalPages
import com.motorro.release.api.MainScreenPageData
import com.motorro.release.data.MainScreenGesture
import com.motorro.release.data.MainScreenViewState

@Composable
fun MainScreen(state: MainScreenViewState, onGesture: (MainScreenGesture) -> Unit, onTerminated: () -> Unit) {
    val onBack = remember { { onGesture(MainScreenGesture.Back) } }
    val onNavigate = remember { { page: MainScreenPageData -> onGesture(MainScreenGesture.Navigate(page)) } }
    val onContentGesture = remember { { page: MainScreenPageData, gesture: Any -> onGesture(MainScreenGesture.PageGesture(page, gesture)) } }

    when (state) {
        MainScreenViewState.Loading -> LoadingScreen(onBack = onBack)
        is MainScreenViewState.Page -> {
            MainScreenContent(
                currentPage = state,
                onBack = onBack,
                onNavigate = onNavigate,
                onContentGesture = onContentGesture
            )
        }
        MainScreenViewState.Terminated -> LaunchedEffect(state) {
            onTerminated()
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MainScreenContent(
    currentPage: MainScreenViewState.Page,
    onBack: () -> Unit,
    onNavigate: (MainScreenPageData) -> Unit,
    onContentGesture: (MainScreenPageData, Any) -> Unit
) {
    val pages = LocalPages.current
    val pageApi = remember(currentPage) { pages.first { currentPage.page == it.data } }

    BackHandler(onBack = onBack)
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(currentPage.page.title)) })
        },
        bottomBar = {
            NavigationBar {
                pages.forEach { page ->
                    NavigationBarItem(
                        selected = currentPage.page == page.data,
                        onClick = { onNavigate(page.data) },
                        label = { Text(stringResource(page.data.title)) },
                        icon = {
                            Icon(
                                painterResource(page.data.icon),
                                contentDescription = stringResource(page.data.title)
                            )
                        }
                    )
                }
            }
        },
        modifier = Modifier.fillMaxSize(),
        content = { contentPadding ->
            pageApi.Screen(
                state = currentPage.viewState,
                onGesture = { onContentGesture(currentPage.page, it) },
                modifier = Modifier.padding(contentPadding)
            )
        }
    )
}
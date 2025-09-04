package com.motorro.statemachine.commonpreview

import android.statemachine.commonpreview.generated.resources.Res
import android.statemachine.commonpreview.generated.resources.auth
import android.statemachine.commonpreview.generated.resources.content
import android.statemachine.commonpreview.generated.resources.error
import android.statemachine.commonpreview.generated.resources.eula
import android.statemachine.commonpreview.generated.resources.loading
import android.statemachine.commonpreview.generated.resources.login
import android.statemachine.commonpreview.generated.resources.registration
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.motorro.statemachine.commonpreview.ui.AuthPromptPreview
import com.motorro.statemachine.commonpreview.ui.ContentPreview
import com.motorro.statemachine.commonpreview.ui.ErrorPreview
import com.motorro.statemachine.commonpreview.ui.LoadingPreview
import com.motorro.statemachine.commonpreview.ui.LoginPreview
import com.motorro.statemachine.commonpreview.ui.RegistrationEulaPreview
import com.motorro.statemachine.commonpreview.ui.RegistrationFormPreview
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun App() {
    var activePage by rememberSaveable { mutableStateOf(Page.Loading) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(activePage.title) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                Page.entries.forEach { page ->
                    NavigationBarItem(
                        selected = activePage == page,
                        onClick = {
                            activePage = page
                        },
                        icon = {
                            Icon(
                                painter = painterResource(page.icon),
                                contentDescription = page.title
                            )
                        },
                        label = { Text(page.title) }
                    )
                }
            }
        }
    ) { paddingValues -> activePage.content(Modifier.padding(paddingValues)) }
}

enum class Page(val title: String, val icon: DrawableResource, val content: @Composable (Modifier) -> Unit) {
    Loading("Loading", Res.drawable.loading, {
        LoadingPreview(it)
    }),
    Error("Error", Res.drawable.error, {
        ErrorPreview(it)
    }),
    AuthPrompt("Authenticate", Res.drawable.auth, {
        AuthPromptPreview(it)
    }),
    Login("Login", Res.drawable.login, {
        LoginPreview(it)
    }),
    RegistrationForm("Registration", Res.drawable.registration, {
        RegistrationFormPreview(it)
    }),
    RegistrationEula("Eula", Res.drawable.eula, {
        RegistrationEulaPreview(it)
    }),
    Content("Content", Res.drawable.content, {
        ContentPreview(it)
    });
}
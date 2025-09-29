package com.motorro.cookbook.loginsocial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.motorro.cookbook.appcore.compose.ui.loading.LoadingScreen
import com.motorro.cookbook.appcore.compose.ui.theme.AppDimens
import com.motorro.cookbook.appcore.compose.ui.theme.CookbookTheme
import com.motorro.cookbook.appcore.compose.ui.theme.appBarColors
import com.motorro.cookbook.loginsocial.data.LoginSocialGesture
import com.motorro.cookbook.loginsocial.data.LoginSocialViewState
import com.motorro.cookbook.appcore.R as ACR

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun LoginSocialScreen(
    state: LoginSocialViewState,
    onGesture: (LoginSocialGesture) -> Unit,
    modifier: Modifier = Modifier
) {
    when(state) {
        is LoginSocialViewState.Buttons -> LoginSocialScreen(
            state = state,
            onGesture = onGesture,
            modifier = modifier
        )
        LoginSocialViewState.Loading -> LoadingScreen(
            title = stringResource(R.string.title_login),
            modifier = modifier
        )
        is LoginSocialViewState.LoggingIn -> LoggingInScreen(
            state = state,
            onGesture = onGesture,
            modifier = modifier
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun LoginSocialScreen(
    state: LoginSocialViewState.Buttons,
    onGesture: (LoginSocialGesture) -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    if (null != state.error) {
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(
                message = state.error,
                withDismissAction = true,
                duration = SnackbarDuration.Indefinite
            )
            onGesture(LoginSocialGesture.DismissError)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.title_login)) },
                navigationIcon = {
                    IconButton(onClick = { onGesture(LoginSocialGesture.Back) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(ACR.string.btn_back)
                        )
                    }
                },
                colors = MaterialTheme.appBarColors()
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .zIndex(1f)
                .fillMaxSize()
                .padding(AppDimens.margin_all),
            verticalArrangement = Arrangement.spacedBy(AppDimens.vertical_margin, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (button in state.buttons) {
                Button(
                    onClick = { onGesture(LoginSocialGesture.LoginSocial(button.id)) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        modifier = Modifier.padding(end = AppDimens.margin_all_small),
                        painter = painterResource(button.icon),
                        contentDescription = button.title
                    )
                    Text(text = button.title)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoggingInScreen(
    state: LoginSocialViewState.LoggingIn,
    onGesture: (LoginSocialGesture) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.title_login)) },
                navigationIcon = {
                    IconButton(onClick = { onGesture(LoginSocialGesture.Back) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(ACR.string.btn_back)
                        )
                    }
                },
                colors = MaterialTheme.appBarColors()
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(AppDimens.margin_all),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = state.providerIcon),
                contentDescription = state.providerTitle,
                modifier = Modifier.size(96.dp)
            )
            Spacer(modifier = Modifier.height(AppDimens.margin_all))
            Text(
                text = stringResource(R.string.logging_in_with, state.providerTitle),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(AppDimens.vertical_margin))
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
@Preview(showBackground = true, name = "Loading State")
private fun PreviewLoginSocialScreenLoading() {
    CookbookTheme {
        LoginSocialScreen(
            state = LoginSocialViewState.Loading,
            onGesture = {}
        )
    }
}

@Preview(showBackground = true, name = "Buttons State - No Error")
@Composable
private fun PreviewLoginSocialScreenButtons() {
    CookbookTheme {
        LoginSocialScreen(
            state = LoginSocialViewState.Buttons(
                buttons = listOf(
                    LoginSocialViewState.Buttons.Button(
                        id = "social",
                        title = "Social network",
                        icon = R.drawable.ic_social
                    ),
                    LoginSocialViewState.Buttons.Button(
                        id = "other_provider",
                        title = "Email service",
                        icon = R.drawable.ic_email
                    )
                ),
                error = null
            ),
            onGesture = {}
        )
    }
}

@Preview(showBackground = true, name = "Buttons State - With Error")
@Composable
private fun PreviewLoginSocialScreenButtonsWithError() {
    CookbookTheme {
        LoginSocialScreen(
            state = LoginSocialViewState.Buttons(
                buttons = listOf(
                    LoginSocialViewState.Buttons.Button(
                        id = "social",
                        title = "Social network",
                        icon = R.drawable.ic_social
                    ),
                    LoginSocialViewState.Buttons.Button(
                        id = "other_provider",
                        title = "Email service",
                        icon = R.drawable.ic_email
                    )
                ),
                error = "Oops! Something went wrong. Please try again."
            ),
            onGesture = {}
        )
    }
}

@Preview(showBackground = true, name = "Logging In State")
@Composable
private fun PreviewLoginSocialScreenLoggingIn() {
    CookbookTheme {
        LoginSocialScreen(
            state = LoginSocialViewState.LoggingIn(
                providerTitle = "Social network",
                providerIcon = R.drawable.ic_social
            ),
            onGesture = {}
        )
    }
}


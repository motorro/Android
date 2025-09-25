package com.motorro.cookbook.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.motorro.cookbook.appcore.compose.ui.theme.AppDimens
import com.motorro.cookbook.appcore.compose.ui.theme.appBarColors
import com.motorro.cookbook.login.data.LoginGesture
import com.motorro.cookbook.login.data.LoginViewState
import com.motorro.cookbook.appcore.R as ACR

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LoginScreen(
    state: LoginViewState,
    onGesture: (LoginGesture) -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }

    if (state is LoginViewState.LoggedIn) {
        LaunchedEffect(state) {
            onComplete()
        }
        return
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.title_login)) },
                navigationIcon = {
                    IconButton(onClick = { onGesture(LoginGesture.Back) }) {
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
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            if (state is LoginViewState.Loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth().zIndex(2f).align(Alignment.TopCenter))
            }

            Column(
                modifier = Modifier
                    .zIndex(1f)
                    .align(Alignment.TopCenter)
                    .fillMaxSize()
                    .padding(AppDimens.margin_all),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = state.username,
                    onValueChange = { onGesture(LoginGesture.LoginChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(id = R.string.hint_username)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true,
                    enabled = state.controlsEnabled
                )

                Spacer(modifier = Modifier.height(AppDimens.spacer_small))

            OutlinedTextField(
                value = state.password,
                onValueChange = { onGesture(LoginGesture.PasswordChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.hint_password)) },
                singleLine = true,
                enabled = state.controlsEnabled,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Default.Visibility else Icons.Filled.VisibilityOff
                    val description = if (passwordVisible) stringResource(R.string.hide_password) else stringResource(R.string.show_password)

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, description)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(AppDimens.spacer))

            Button(
                onClick = { onGesture(LoginGesture.Login) },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.controlsEnabled && state.loginEnabled
            ) {
                Text(stringResource(id = R.string.btn_login))
            }

                if (state is LoginViewState.Error) {
                    Spacer(modifier = Modifier.height(AppDimens.spacer))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.onError,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(AppDimens.margin_all)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

// --- Previews ---
@Preview(showBackground = true, name = "Form State")
@Composable
fun PreviewLoginScreenForm() {
    MaterialTheme { // Wrap with your app's theme or MaterialTheme for preview
        LoginScreen(
            state = LoginViewState.Form("user", "pass", loginEnabled = true),
            onGesture = {},
            onComplete = {},
        )
    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
fun PreviewLoginScreenLoading() {
    MaterialTheme {
        LoginScreen(
            state = LoginViewState.Loading("user", "pass"),
            onGesture = {},
            onComplete = {},
        )
    }
}

@Preview(showBackground = true, name = "Error State")
@Composable
fun PreviewLoginScreenError() {
    MaterialTheme {
        LoginScreen(
            state = LoginViewState.Error("Incorrect login or password", "user", "pass", loginEnabled = true),
            onGesture = {},
            onComplete = {},
        )
    }
}

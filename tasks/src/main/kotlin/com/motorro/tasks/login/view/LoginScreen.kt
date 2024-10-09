package com.motorro.tasks.login.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.motorro.composecore.ui.ScreenScaffold
import com.motorro.tasks.R
import com.motorro.tasks.login.data.LoginGesture
import com.motorro.tasks.login.data.LoginUiState

@Composable
fun LoginScreen(state: LoginUiState.Form, onGesture: (LoginGesture) -> Unit) {
    ScreenScaffold(
        title = stringResource(R.string.title_login),
        onBack = { onGesture(LoginGesture.Back) },
        content = { padding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(R.string.title_login),
                    style = MaterialTheme.typography.titleLarge
                )
                TextField(
                    modifier = Modifier.padding(16.dp),
                    value = state.userName,
                    onValueChange = { onGesture(LoginGesture.UserNameChanged(it)) },
                    label = { Text(stringResource(R.string.hint_username)) }
                )
                TextField(
                    modifier = Modifier.padding(16.dp),
                    value = state.password,
                    onValueChange = { onGesture(LoginGesture.PasswordChanged(it)) },
                    label = { Text(stringResource(R.string.hint_password)) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )
                Button(
                    modifier = Modifier.padding(16.dp),
                    onClick = { onGesture(LoginGesture.Action) },
                    enabled = state.loginEnabled
                ) {
                    Text(stringResource(R.string.btn_login))
                }
            }
        }
    )
}
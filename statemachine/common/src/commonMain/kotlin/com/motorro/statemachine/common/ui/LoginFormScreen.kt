package com.motorro.statemachine.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.motorro.statemachine.common.Res
import com.motorro.statemachine.common.data.gesture.LoginGesture
import com.motorro.statemachine.common.data.ui.LoginUiState
import com.motorro.statemachine.common.login
import com.motorro.statemachine.common.password
import com.motorro.statemachine.common.username
import org.jetbrains.compose.resources.stringResource

/**
 * A composable function that displays a login screen.
 *
 * @param state The current state of the login screen, defined by [LoginUiState].
 * @param onGesture A callback function to handle UI gestures, emitting [LoginGesture] events.
 */
@Composable
fun LoginFormScreen(state: LoginUiState.Form, modifier: Modifier = Modifier, onGesture: (LoginGesture) -> Unit) {
    BackHandler { onGesture(LoginGesture.Back) }
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.login),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = state.username,
            onValueChange = { onGesture(LoginGesture.UsernameChanged(it)) },
            label = { Text(stringResource(Res.string.username)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = { onGesture(LoginGesture.PasswordChanged(it)) },
            label = { Text(stringResource(Res.string.password)) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Login Button
        Button(
            onClick = { onGesture(LoginGesture.Action) },
            enabled = state.loginEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(Res.string.login))
        }
    }
}

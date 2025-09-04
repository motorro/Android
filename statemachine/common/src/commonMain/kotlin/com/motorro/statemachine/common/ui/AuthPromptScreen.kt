package com.motorro.statemachine.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.motorro.statemachine.common.Res
import com.motorro.statemachine.common.auth_prompt
import com.motorro.statemachine.common.data.gesture.AuthPromptGesture
import com.motorro.statemachine.common.data.ui.AuthPromptUiState
import com.motorro.statemachine.common.login
import com.motorro.statemachine.common.register
import org.jetbrains.compose.resources.stringResource

/**
 * A composable function that displays an authentication prompt screen, allowing users to choose
 * between logging in or registering.
 *
 * @param state The current state of the authentication prompt, defined by [com.motorro.statemachine.common.data.ui.AuthPromptUiState].
 * @param onGesture A callback function to handle UI gestures, emitting [AuthPromptGesture] events.
 */
@Composable
fun AuthPromptScreen(state: AuthPromptUiState, modifier: Modifier = Modifier, onGesture: (AuthPromptGesture) -> Unit) {
    BackHandler { onGesture(AuthPromptGesture.Back) }
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.auth_prompt),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        Button(
            onClick = { onGesture(AuthPromptGesture.LoginClicked) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(Res.string.login))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onGesture(AuthPromptGesture.RegistrationClicked) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(Res.string.register))
        }
    }
}

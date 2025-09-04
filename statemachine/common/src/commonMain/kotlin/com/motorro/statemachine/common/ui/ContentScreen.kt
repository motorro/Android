package com.motorro.statemachine.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.motorro.statemachine.common.Res
import com.motorro.statemachine.common.data.gesture.ContentGesture
import com.motorro.statemachine.common.data.ui.ContentScreenUiState
import com.motorro.statemachine.common.logout
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * A composable function that displays the main content screen after a user has authenticated.
 * It shows a welcome message and a logout button.
 *
 * @param state The current state of the content screen, defined by [ContentScreenUiState].
 * @param onGesture A callback function to handle UI gestures, emitting [ContentGesture] events.
 */
@Composable
fun ContentScreen(state: ContentScreenUiState, modifier: Modifier = Modifier, onGesture: (ContentGesture) -> Unit) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome, ${state.userName}!",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onGesture(ContentGesture.Logout) },
            modifier = Modifier.padding(ButtonDefaults.TextButtonWithIconContentPadding)
        ) {
            Icon(
                modifier = Modifier.size(ButtonDefaults.IconSize).padding(end = ButtonDefaults.IconSpacing),
                painter = painterResource(Res.drawable.logout),
                contentDescription = stringResource(Res.string.logout),
                tint = ButtonDefaults.buttonColors().contentColor
            )
            Text(text = stringResource(Res.string.logout))
        }
    }
}

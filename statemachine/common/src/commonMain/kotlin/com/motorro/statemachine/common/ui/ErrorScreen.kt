package com.motorro.statemachine.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.motorro.statemachine.common.Res
import com.motorro.statemachine.common.close
import com.motorro.statemachine.common.data.gesture.ErrorGesture
import com.motorro.statemachine.common.data.ui.ErrorUiState
import com.motorro.statemachine.common.error
import com.motorro.statemachine.common.retry
import org.jetbrains.compose.resources.stringResource

/**
 * Displays an error screen with a header, message, and a dismiss/retry button.
 *
 * @param error The error state to display, containing a message and a retry flag.
 * @param modifier The modifier to be applied to the layout.
 * @param onGesture Callback to be invoked when the action button (Retry/Close) is clicked.
 */
@Composable
fun ErrorScreen(error: ErrorUiState, modifier: Modifier = Modifier, onGesture: (ErrorGesture) -> Unit) {
    BackHandler(onBack = { onGesture(ErrorGesture.Back) })
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.error),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = error.message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onGesture(ErrorGesture.Action) },
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = if (error.canRetry) {
                    stringResource(Res.string.retry)
                } else {
                    stringResource(Res.string.close)
                }
            )
        }
    }
}

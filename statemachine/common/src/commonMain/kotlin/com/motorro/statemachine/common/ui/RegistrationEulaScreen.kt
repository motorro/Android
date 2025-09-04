package com.motorro.statemachine.common.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.motorro.statemachine.common.Res
import com.motorro.statemachine.common.accept_eula
import com.motorro.statemachine.common.data.gesture.RegistrationGesture
import com.motorro.statemachine.common.data.ui.RegistrationUiState
import com.motorro.statemachine.common.eula
import com.motorro.statemachine.common.next
import org.jetbrains.compose.resources.stringResource

/**
 * A composable function that displays the End-User License Agreement (EULA) screen.
 *
 * @param state The current state of the EULA screen, defined by [RegistrationUiState.Eula].
 * @param onGesture A callback function to handle UI gestures, emitting [RegistrationGesture] events.
 */
@Composable
fun RegistrationEulaScreen(state: RegistrationUiState.Eula, modifier: Modifier = Modifier, onGesture: (RegistrationGesture) -> Unit) {
    BackHandler { onGesture(RegistrationGesture.Back) }
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.eula),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        val scrollState: ScrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(bottom = 16.dp)
                .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium)
                .padding(16.dp)
        ) {
            Text(
                text = state.eula,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(
                checked = state.accepted,
                onCheckedChange = { onGesture(RegistrationGesture.EulaToggled) }
            )
            Text(
                text = stringResource(Res.string.accept_eula),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onGesture(RegistrationGesture.Action) },
            enabled = state.nextEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(Res.string.next))
        }
    }
}

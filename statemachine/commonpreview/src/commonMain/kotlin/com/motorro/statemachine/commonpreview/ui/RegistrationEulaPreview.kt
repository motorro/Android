package com.motorro.statemachine.commonpreview.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.motorro.statemachine.common.data.gesture.RegistrationGesture
import com.motorro.statemachine.common.data.ui.RegistrationUiState
import com.motorro.statemachine.common.ui.RegistrationEulaScreen

@Composable
fun RegistrationEulaPreview(modifier: Modifier = Modifier) {
    var accepted by remember { mutableStateOf(false) }

    RegistrationEulaScreen(
        state = RegistrationUiState.Eula(
            eula = "Please accept the EULA",
            accepted = accepted,
            nextEnabled = accepted
        ),
        modifier = modifier,
        onGesture = {
            when (it) {
                is RegistrationGesture.EulaToggled -> accepted = accepted.not()
                else -> { }
            }
        }
    )
}
package com.motorro.statemachine.commonpreview.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.motorro.statemachine.common.data.gesture.RegistrationGesture
import com.motorro.statemachine.common.data.ui.RegistrationUiState
import com.motorro.statemachine.common.ui.RegistrationFormScreen

@Composable
fun RegistrationFormPreview(modifier: Modifier = Modifier) {
    var user by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val nextEnabled by remember { derivedStateOf { user.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty() } }

    RegistrationFormScreen(
        state = RegistrationUiState.Form(
            username = user,
            password = password,
            email = email,
            nextEnabled = nextEnabled
        ),
        modifier = modifier,
        onGesture = {
            when (it) {
                is RegistrationGesture.UsernameChanged -> user = it.value
                is RegistrationGesture.PasswordChanged -> password = it.value
                is RegistrationGesture.EmailChanged -> email = it.value
                else -> { }
            }
        }
    )
}
package com.motorro.statemachine.commonpreview.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.motorro.statemachine.common.data.gesture.LoginGesture
import com.motorro.statemachine.common.data.ui.LoginUiState
import com.motorro.statemachine.common.ui.LoginFormScreen

@Composable
fun LoginPreview(modifier: Modifier = Modifier) {
    var user by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginEnabled by remember { derivedStateOf { user.isNotEmpty() && password.isNotEmpty() } }

    LoginFormScreen(
        state = LoginUiState.Form(
            username = user,
            password = password,
            loginEnabled = loginEnabled
        ),
        modifier = modifier,
        onGesture = {
            when (it) {
                is LoginGesture.UsernameChanged -> user = it.value
                is LoginGesture.PasswordChanged -> password = it.value
                else -> { }
            }
        }
    )
}
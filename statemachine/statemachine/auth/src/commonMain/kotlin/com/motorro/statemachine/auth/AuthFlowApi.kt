package com.motorro.statemachine.auth

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.statemachine.auth.data.AuthFlowGesture
import com.motorro.statemachine.auth.data.AuthFlowUiState
import com.motorro.statemachine.common.api.AuthFlowHost
import com.motorro.statemachine.common.data.gesture.AuthPromptGesture
import com.motorro.statemachine.common.data.ui.AuthPromptUiState
import com.motorro.statemachine.common.ui.AuthPromptScreen
import com.motorro.statemachine.common.ui.ScreenScaffold
import com.motorro.statemachine.login.LoginScreen
import com.motorro.statemachine.register.RegisterScreen
import org.jetbrains.compose.resources.stringResource

/**
 * Module API
 */
interface AuthFlowApi {
    /**
     * Starts login flow
     */
    fun start(flowHost: AuthFlowHost): CommonMachineState<AuthFlowGesture, AuthFlowUiState>

    companion object Companion : AuthFlowApi {
        override fun start(flowHost: AuthFlowHost): CommonMachineState<AuthFlowGesture, AuthFlowUiState> {
            return AuthPromptState(flowHost)
        }

        val DEFAULT_UI_STATE = AuthFlowUiState.Prompt
    }
}

/**
 * Module view
 */
@Composable
fun AuthScreen(state: AuthFlowUiState, onGesture: (AuthFlowGesture) -> Unit) {
    when(state) {
        AuthFlowUiState.Prompt -> ScreenScaffold(
            title = { Text(stringResource(Res.string.title_authenticate)) },
            onBack = { onGesture(AuthFlowGesture.Back) },
            content = { paddingValues ->
                AuthPromptScreen(
                    state = AuthPromptUiState,
                    modifier = Modifier.padding(paddingValues),
                    onGesture = {
                        when(it) {
                            AuthPromptGesture.Back -> onGesture(AuthFlowGesture.Back)
                            AuthPromptGesture.LoginClicked -> onGesture(AuthFlowGesture.LoginPressed)
                            AuthPromptGesture.RegistrationClicked -> onGesture(AuthFlowGesture.RegisterPressed)
                        }
                    }
                )
            }
        )
        is AuthFlowUiState.Login -> LoginScreen(state.child) {
            onGesture(AuthFlowGesture.Login(it))
        }
        is AuthFlowUiState.Registration -> RegisterScreen(state.child) {
            onGesture(AuthFlowGesture.Registration(it))
        }
    }
}

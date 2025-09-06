package com.motorro.statemachine.navigation.login

import androidx.lifecycle.ViewModel
import com.motorro.statemachine.common.data.ui.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Form("", "", false))

    /**
     * View-state.
     */
    val uiState: StateFlow<LoginUiState> get() = _uiState

    private fun LoginUiState.Form.isValid() = username.isNotBlank() && password.isNotBlank()

    private fun LoginUiState.Form.reduce(username: String? = null , password: String? = null) = copy(
        username = username ?: this.username,
        password = password ?: this.password,
        loginEnabled = isValid()
    )

    fun setUsername(username: String) {
        // Checking valid state
        val state = _uiState.value as? LoginUiState.Form ?: return

        _uiState.update {
            state.reduce(username = username)
        }
    }

    fun setPassword(password: String) {
        // Checking valid state
        val state = _uiState.value as? LoginUiState.Form ?: return

        _uiState.update {
            state.reduce(password = password)
        }
    }
}
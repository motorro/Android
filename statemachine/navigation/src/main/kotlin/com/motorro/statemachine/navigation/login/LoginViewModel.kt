package com.motorro.statemachine.navigation.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.motorro.statemachine.common.data.exception.toAppException
import com.motorro.statemachine.common.data.ui.ErrorUiState
import com.motorro.statemachine.common.data.ui.LoadingUiState
import com.motorro.statemachine.common.data.ui.LoginUiState
import com.motorro.statemachine.common.session.SessionManager
import com.motorro.statemachine.navigation.R
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Form("", "", false))

    /**
     * View-state.
     */
    val uiState: StateFlow<LoginUiState> get() = _uiState

    private val _navigationEvents = MutableSharedFlow<LoginNavigationEvent>(1)

    /**
     * Navigation events to be observed by the UI.
     */
    val navigationEvents: SharedFlow<LoginNavigationEvent> = _navigationEvents

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

    var savedUsername: String? = null
    var savedPassword: String? = null

    fun login() {
        // Checking valid state
        val state = _uiState.value as? LoginUiState.Form ?: return

        savedUsername = state.username
        savedPassword = state.password

        if (state.isValid()) {
            doLogin(state.username, state.password)
        }
    }

    fun retry() {
        // Checking valid state
        val state = _uiState.value as? LoginUiState.Error ?: return
        val username = savedUsername
        val password = savedPassword

        if (state.state.canRetry && username != null && password != null) {
            doLogin(username, password)
        }
    }

    private fun doLogin(username: String, password: String) {
        _uiState.value = LoginUiState.Loading(LoadingUiState(application.getString(R.string.logging_in)))

        viewModelScope.launch {
            try {
                SessionManager.Instance.login(username, password)
                _navigationEvents.emit(LoginNavigationEvent.NavigateToContent)
            } catch (e: Throwable) {
                currentCoroutineContext().ensureActive()
                val error = e.toAppException()
                _uiState.value = LoginUiState.Error(ErrorUiState(
                    error.message,
                    error.isFatal.not()
                ))
            }
        }
    }
}
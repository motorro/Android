package com.motorro.cookbook.app.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.motorro.cookbook.app.App
import com.motorro.cookbook.app.session.SessionManager
import com.motorro.cookbook.app.session.data.LoginViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModel(private val sessionManager: SessionManager) : ViewModel() {

    private val login = MutableStateFlow("")
    private val password = MutableStateFlow("")
    private val state = MutableStateFlow<LoginOperationState>(LoginOperationState.Idle)

    val viewState: StateFlow<LoginViewState> = state.flatMapLatest {
        when(it) {
            LoginOperationState.Complete -> flowOf(
                LoginViewState.LoggedIn
            )
            is LoginOperationState.Error -> combine(login, password) { l, p ->
                LoginViewState.Error(
                    it.message,
                    l,
                    p,
                    loginEnabled(l, p)
                )
            }
            LoginOperationState.Idle -> combine(login, password) { l, p ->
                LoginViewState.Form(
                    l,
                    p,
                    loginEnabled(l, p)
                )
            }
            LoginOperationState.Loading -> flowOf(
                LoginViewState.Loading(
                    login.value,
                    password.value,
                )
            )
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        LoginViewState.Form(login.value, password.value, loginEnabled(login.value, password.value))
    )

    fun setLogin(login: String) {
        this.login.tryEmit(login)
    }

    fun setPassword(password: String) {
        this.password.tryEmit(password)
    }

    fun login() {
        if (loginEnabled(login.value, password.value)) viewModelScope.launch {
            state.emit(LoginOperationState.Loading)
            sessionManager.login(login.value, password.value)
                .onSuccess { state.tryEmit(LoginOperationState.Complete) }
                .onFailure { state.tryEmit(LoginOperationState.Error(it.message ?: "Unknown error")) }
        }
    }

    private fun loginEnabled(login: String, password: String): Boolean = login.isNotBlank() && password.isNotBlank()

    private sealed class LoginOperationState {
        data object Idle : LoginOperationState()
        data object Loading : LoginOperationState()
        data object Complete : LoginOperationState()
        data class Error(val message: String) : LoginOperationState()
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(context: Context) : ViewModelProvider.Factory {

        private val app: App = context.applicationContext as App

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel(app.sessionManager) as T
        }
    }
}
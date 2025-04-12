package com.motorro.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.motorro.network.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AuthFragmentModel(private val sessionManager: SessionManager) : ViewModel() {

    private val _token: MutableStateFlow<String?> = MutableStateFlow(sessionManager.token.value)
    val token: StateFlow<String?> get() = _token.asStateFlow()
    fun updateToken(token: String) {
        _token.value = token.takeIf { it.isNotBlank() }
    }

    val saveEnabled: StateFlow<Boolean> = _token
        .map { it != sessionManager.token.value }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false
        )

    fun save() {
        sessionManager.saveToken(_token.value)
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val app: App): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthFragmentModel::class.java)) {
                return AuthFragmentModel(app.sessionManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
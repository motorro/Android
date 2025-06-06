package com.motorro.architecture.account.russian

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.motorro.architecture.account.russian.data.UiState
import com.motorro.architecture.domain.session.SESSION_DATA
import com.motorro.architecture.domain.session.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

/**
 * Emulates social network login
 */
class RussianViewModel(private val sessionManager: SessionManager) : ViewModel() {

    private val collector = MutableStateFlow<UiState>(UiState.Prompt)

    private val login = flow {
        emit(UiState.LoggingIn)
        delay(1000)
        sessionManager.login(SESSION_DATA)
        emit(UiState.Complete)
    }

    /**
     * Ui state
     */
    val uiState: StateFlow<UiState> get() = collector.asStateFlow()

    /**
     * Logs user in
     */
    fun login() = viewModelScope.launch {
        login.collect(collector)
    }
}
package com.motorro.statemachine.navigation.content

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.motorro.statemachine.common.data.ui.ContentScreenUiState
import com.motorro.statemachine.common.data.ui.LoadingUiState
import com.motorro.statemachine.common.session.SessionManager
import com.motorro.statemachine.common.session.data.Session
import com.motorro.statemachine.navigation.R
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the content screen, managing its UI state and navigation events.
 */
class ContentViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<ContentScreenState>(ContentScreenState.Loading(LoadingUiState(application.getString(R.string.loading))))

    /**
     * View-state.
     */
    val uiState: StateFlow<ContentScreenState> get() = _uiState

    private val _navigationEvents = MutableSharedFlow<ContentNavigationEvent>()

    /**
     * Navigation events to be observed by the UI.
     */
    val navigationEvents: SharedFlow<ContentNavigationEvent> = _navigationEvents

    init {
        // Listens to the Session Manager.
        // If we have active session - shows content.
        // If we don't - navigates to login.
        viewModelScope.launch {
            SessionManager.Instance.session.collect { session ->
                when (session) {
                    is Session.Active -> {
                        _uiState.value = ContentScreenState.Content(ContentScreenUiState(session.user.username))
                    }
                    Session.NotLoggedIn -> {
                        navigateToLogin()
                    }
                }
            }
        }
    }

    /**
     * Triggers a navigation event to the login fragment/screen.
     */
    private fun navigateToLogin() = viewModelScope.launch {
        _navigationEvents.emit(ContentNavigationEvent.NavigateToLogin)
    }

    /**
     * Triggers a navigation event when user clicks logout
     */
    fun logout() = viewModelScope.launch {
        _navigationEvents.emit(ContentNavigationEvent.NavigateToLogout)
    }
}
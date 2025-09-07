package com.motorro.statemachine.navigation.logout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.motorro.statemachine.common.session.SessionManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class LogoutViewModel : ViewModel() {
    private val _navigationEvents = MutableSharedFlow<LogoutNavigationEvent>(1)

    /**
     * Navigation events to be observed by the UI.
     */
    val navigationEvents: SharedFlow<LogoutNavigationEvent> = _navigationEvents

    init {
        viewModelScope.launch {
            SessionManager.Instance.logout()
            _navigationEvents.emit(LogoutNavigationEvent.NavigateToContent)
        }
    }
}
package com.motorro.architecture.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.motorro.architecture.core.error.CoreException
import com.motorro.architecture.core.lce.LceState
import com.motorro.architecture.domain.profile.error.NotRegisteredException
import com.motorro.architecture.domain.profile.usecase.GetCurrentUserProfileUsecase
import com.motorro.architecture.domain.session.error.UnauthorizedException
import com.motorro.architecture.main.data.MainScreenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Main fragment view-model
 */
class MainViewModel(registration: Flow<LceState<Any, CoreException>>): ViewModel() {
    private val collector = MutableStateFlow<MainScreenState>(MainScreenState.Content)

    /**
     * View state
     */
    val state: StateFlow<MainScreenState> = collector.asStateFlow()

    init {
        viewModelScope.launch {
            registration
                .map { state ->
                    when {
                        state is LceState.Error && state.error is UnauthorizedException -> MainScreenState.Authenticating
                        state is LceState.Error && state.error is NotRegisteredException -> MainScreenState.Registering
                        else -> MainScreenState.Content
                    }
                }
                .collect(collector)
        }
    }

    /**
     * View-model factory
     */
    class Factory(private val getProfile: GetCurrentUserProfileUsecase) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(getProfile()) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
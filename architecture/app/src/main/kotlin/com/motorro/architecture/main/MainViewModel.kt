package com.motorro.architecture.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.motorro.architecture.core.error.CoreException
import com.motorro.architecture.core.lce.LceState
import com.motorro.architecture.domain.profile.usecase.GetCurrentUserProfileUsecase
import com.motorro.architecture.model.user.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Main fragment view-model
 */
class MainViewModel(getProfile: GetCurrentUserProfileUsecase): ViewModel() {
    private val collector = MutableStateFlow<LceState<UserProfile, CoreException>>(LceState.Loading())

    /**
     * View state
     */
    val state: StateFlow<LceState<UserProfile, CoreException>> = collector.asStateFlow()

    init {
        viewModelScope.launch {
            getProfile().collect(collector)
        }
    }

    /**
     * View-model factory
     */
    class Factory(private val getProfile: GetCurrentUserProfileUsecase) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(getProfile) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
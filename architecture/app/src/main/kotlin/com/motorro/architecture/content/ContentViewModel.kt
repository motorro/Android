package com.motorro.architecture.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.motorro.architecture.core.error.CoreException
import com.motorro.architecture.core.lce.LceState
import com.motorro.architecture.domain.profile.usecase.GetCurrentUserProfileUsecase
import com.motorro.architecture.model.user.UserProfile
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Content fragment view-model
 */
class ContentViewModel(private val getCurrentUserProfileUsecase: GetCurrentUserProfileUsecase) : ViewModel() {

    private var collectJob: Job? = null
    private val collector = MutableStateFlow<LceState<UserProfile, CoreException>>(LceState.Loading())

    init {
        collect()
    }

    private fun collect() {
        collectJob?.cancel()
        collectJob = viewModelScope.launch {
            getCurrentUserProfileUsecase().collect(collector)
        }
    }

    /**
     * View state
     */
    val state: StateFlow<LceState<UserProfile, CoreException>> get() = collector.asStateFlow()

    /**
     * Retry when error
     */
    fun retry() {
        collect()
    }

    /**
     * View-model factory
     */
    class Factory(private val getCurrentUserProfileUsecase : GetCurrentUserProfileUsecase) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ContentViewModel::class.java)) {
                return ContentViewModel(getCurrentUserProfileUsecase) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
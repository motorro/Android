package com.motorro.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.motorro.network.data.Profile
import com.motorro.network.net.usecase.GetProfile
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class ProfileViewModel(
    private val id: Int,
    private val getProfile: GetProfile
): ViewModel() {

    val profile: StateFlow<Profile?> get() = flow { emit(getProfile(id).getOrNull()) }.stateIn(
        viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = null
    )

    @Suppress("UNCHECKED_CAST")
    class Factory(private val userId: Int, private val app: App): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                return ProfileViewModel(userId, app.getProfile) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
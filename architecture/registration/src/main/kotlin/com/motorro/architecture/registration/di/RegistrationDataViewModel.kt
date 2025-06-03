package com.motorro.architecture.registration.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.motorro.architecture.model.user.CountryCode
import com.motorro.architecture.registration.Constants.StateKeys
import com.motorro.architecture.registration.data.RegistrationData
import com.motorro.architecture.registration.data.Storage
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Stores registration data across fragment transitions
 * and preserves it to [savedStateHandle]
 * Uses [ViewModel] under the hood
 */
class RegistrationDataViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel(), RegistrationData {

    override val name: Storage<String> = object : Storage<String> {
        override val value: StateFlow<String> = savedStateHandle.getStateFlow(StateKeys.NAME_KEY, "")
        override fun update(value: String) { savedStateHandle[StateKeys.NAME_KEY] = value }
    }

    override val country: Storage<CountryCode> = object : Storage<CountryCode> {
        override val value: StateFlow<CountryCode> = savedStateHandle.getStateFlow(StateKeys.COUNTRY_KEY, "").map(::CountryCode).stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            CountryCode(savedStateHandle.get<String>(StateKeys.COUNTRY_KEY).orEmpty())
        )
        override fun update(value: CountryCode) { savedStateHandle[StateKeys.COUNTRY_KEY] = value }
    }

    companion object {
        /**
         * View-model factory
         */
        val Factory = viewModelFactory {
            initializer {
                RegistrationDataViewModel(createSavedStateHandle())
            }
        }
    }
}
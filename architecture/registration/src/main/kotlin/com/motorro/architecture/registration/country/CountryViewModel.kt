package com.motorro.architecture.registration.country

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.motorro.architecture.core.log.Logging
import com.motorro.architecture.model.user.CountryCode
import com.motorro.architecture.registration.data.Storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

/**
 * View-model for country fragment
 */
internal class CountryViewModel(
    private val storage: Storage<CountryCode>,
    private val countryList: List<Pair<CountryCode, String>>
) : ViewModel(), Logging {

    private val _countryInput = MutableStateFlow(getSavedCountryName())

    /**
     * Country input value
     */
    val countryInput: StateFlow<String> get() = _countryInput.asStateFlow()

    /**
     * Country dropdown list
     */
    val countrySelect: StateFlow<List<String>> = _countryInput
        .map { input ->
            countryList.mapTo(mutableListOf()) { it.second }.apply {
                if (input.isNotBlank()) {
                    retainAll { it.startsWith(input, ignoreCase = true) }
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    /**
     * If true - country is valid
     */
    val countryIsValid: StateFlow<Boolean> = storage.value
        .map { CountryCode.UNKNOWN != it }
        .onEach { d { "Country is valid: $it" } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false
        )

    /**
     * Updates country name
     */
    fun setCountryName(value: String) {
        _countryInput.value = value
        saveCountryByName(value)
    }

    /**
     * Finds country by saved code
     */
    private fun getSavedCountryName(): String {
        val savedCode = storage.value.value
        return countryList.find { (code, _) -> savedCode == code}?.second.orEmpty()
    }

    /**
     * Saves country code if found
     */
    private fun saveCountryByName(input: String) {
        storage.update(countryList.find { (_, name) -> name.equals(input, ignoreCase = true) }?.first ?: CountryCode.UNKNOWN)
    }
}
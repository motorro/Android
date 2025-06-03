package com.motorro.architecture.registration.name

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.motorro.architecture.core.log.Logging
import com.motorro.architecture.registration.data.Storage
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

/**
 * View-model for the name fragment
 */
internal class NameViewModel(private val storage: Storage<String>, minNameLength: Int) : ViewModel(), Logging {
    /**
     * Name value
     */
    val name: StateFlow<String> get() = storage.value

    /**
     * If true - name is valid
     */
    val nameIsValid: StateFlow<Boolean> = name
        .map { it.isNotBlank() && it.length >= minNameLength }
        .onEach { d { "Name is valid: $it" } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false
        )

    /**
     * Sets new names
     */
    fun setName(value: String) {
        d { "Name updated: $value" }
        storage.update(value)
    }
}
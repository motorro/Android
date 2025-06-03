package com.motorro.architecture.registration

import com.motorro.architecture.registration.data.Storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TestStorage<T>(initial: T): Storage<T> {
    private val state = MutableStateFlow(initial)
    override val value: StateFlow<T> = state.asStateFlow()
    override fun update(value: T) = state.update { value }
}
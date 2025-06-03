package com.motorro.architecture.registration.data

import kotlinx.coroutines.flow.StateFlow

/**
 * Reactive storage of some [T]
 */
interface Storage<T> {
    /**
     * Value flow
     */
    val value: StateFlow<T>

    /**
     * Updates value
     */
    fun update(value: T)
}
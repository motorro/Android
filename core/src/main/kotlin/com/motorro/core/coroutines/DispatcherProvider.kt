package com.motorro.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Coroutines dispatchers abstraction to make testing easier
 */
interface DispatcherProvider {
    /**
     * Main dispatcher
     */
    val main: CoroutineDispatcher

    /**
     * Default dispatcher
     */
    val default: CoroutineDispatcher

    /**
     * IO dispatcher
     */
    val io: CoroutineDispatcher
}
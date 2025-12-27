@file:OptIn(ExperimentalCoroutinesApi::class)

package com.motorro.tasks

import com.motorro.core.coroutines.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher

internal class TestDispatchers(dispatcher: TestDispatcher = UnconfinedTestDispatcher()): DispatcherProvider {
    override val main: CoroutineDispatcher = dispatcher
    override val default: CoroutineDispatcher = dispatcher
    override val io: CoroutineDispatcher = dispatcher
}
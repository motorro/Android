@file:OptIn(ExperimentalCoroutinesApi::class)

package ru.merionet.tasks

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import ru.merionet.core.coroutines.DispatcherProvider

internal class TestDispatchers(dispatcher: TestDispatcher = UnconfinedTestDispatcher()): DispatcherProvider {
    override val main: CoroutineDispatcher = dispatcher
    override val default: CoroutineDispatcher = dispatcher
    override val io: CoroutineDispatcher = dispatcher
}
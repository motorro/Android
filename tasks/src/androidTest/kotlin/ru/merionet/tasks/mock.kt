@file:OptIn(ExperimentalCoroutinesApi::class)

package ru.merionet.tasks

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import ru.merionet.core.coroutines.DispatcherProvider
import ru.merionet.tasks.auth.data.Session
import ru.merionet.tasks.data.SessionClaims
import ru.merionet.tasks.data.UserName

internal val USER_NAME = UserName("username")

internal val activeSession = Session.Active(
    claims = SessionClaims(
        username = USER_NAME,
        token = "token"
    )
)

internal class TestDispatchers(dispatcher: TestDispatcher = UnconfinedTestDispatcher()): DispatcherProvider {
    override val main: CoroutineDispatcher = dispatcher
    override val default: CoroutineDispatcher = dispatcher
    override val io: CoroutineDispatcher = dispatcher
}
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.motorro.tasks

import com.motorro.core.coroutines.DispatcherProvider
import com.motorro.tasks.auth.data.Session
import com.motorro.tasks.data.SessionClaims
import com.motorro.tasks.data.UserName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher

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
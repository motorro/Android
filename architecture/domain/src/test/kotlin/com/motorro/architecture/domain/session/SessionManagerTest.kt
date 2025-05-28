package com.motorro.architecture.domain.session

import com.motorro.architecture.core.error.CoreException
import com.motorro.architecture.core.lce.LceState
import com.motorro.architecture.domain.session.data.Session
import com.motorro.architecture.domain.session.data.SessionData
import com.motorro.architecture.domain.session.error.UnauthorizedException
import com.motorro.architecture.model.user.UserId
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class SessionManagerTest {
    private lateinit var sessionStorage: SessionStorage

    @Before
    fun init() {
        sessionStorage = mockk()
    }

    @Test
    fun updatesSession() = test {sessionManager ->
        val source = MutableSharedFlow<SessionData?>()
        every { sessionStorage.session } returns source

        val emitted = backgroundScope.async {
            sessionManager.session.take(3).toList()
        }

        source.emit(null)
        source.emit(SESSION_DATA)

        assertEquals(
            listOf(Session.Loading, Session.None, Session.Active(SESSION_DATA)),
            emitted.await()
        )
    }

    @Test
    fun storesSessionOnLogin() = test { sessionManager ->
        coEvery { sessionStorage.update(any()) } just Runs
        sessionManager.login(SESSION_DATA)

        coVerify { sessionStorage.update(SESSION_DATA) }
    }

    @Test
    fun storesSessionOnLogout() = test { sessionManager ->
        coEvery { sessionStorage.update(any()) } just Runs
        sessionManager.logout()

        coVerify { sessionStorage.update(null) }
    }

    @Test
    fun returnsUserIdWhenActive() = test { sessionManager ->
        every { sessionStorage.session } returns flowOf(SESSION_DATA)
        assertEquals(
            SESSION_DATA.userId,
            sessionManager.requireUserId()
        )
    }

    @Test
    fun throwsUnauthorizedExceptionWhenNoUser() = test { sessionManager ->
        every { sessionStorage.session } returns flowOf(null)
        assertFailsWith<UnauthorizedException> {
            sessionManager.requireUserId()
        }
    }

    @Test
    fun flatMapsActiveSession() = test {sessionManager ->
        every { sessionStorage.session } returns flowOf(SESSION_DATA)
        val emitted = backgroundScope.async {
            sessionManager.flatMapUserId { flowOf(LceState.Content(it)) }.take(2).toList()
        }

        assertEquals(
            listOf<LceState<UserId, CoreException>>(LceState.Loading(), LceState.Content(SESSION_DATA.userId)),
            emitted.await()
        )
    }

    @Test
    fun flatMapsToErrorWhenNotAuthenticated() = test {sessionManager ->
        every { sessionStorage.session } returns flowOf(null)
        val emitted = backgroundScope.async {
            sessionManager.flatMapUserId { flowOf(LceState.Content(it)) }.take(2).toList()
        }

        assertEquals(
            listOf<LceState<UserId, CoreException>>(LceState.Loading(), LceState.Error(UnauthorizedException())),
            emitted.await()
        )
    }

    @Test
    fun transformsActiveSession() = test {sessionManager ->
        every { sessionStorage.session } returns flowOf(SESSION_DATA)
        val emitted = backgroundScope.async {
            sessionManager.transformUserId { emit(LceState.Content(it)) }.take(2).toList()
        }

        assertEquals(
            listOf<LceState<UserId, CoreException>>(LceState.Loading(), LceState.Content(SESSION_DATA.userId)),
            emitted.await()
        )
    }

    @Test
    fun transformsToErrorWhenNotAuthenticated() = test {sessionManager ->
        every { sessionStorage.session } returns flowOf(null)
        val emitted = backgroundScope.async {
            sessionManager.transformUserId { emit(LceState.Content(it)) }.take(2).toList()
        }

        assertEquals(
            listOf<LceState<UserId, CoreException>>(LceState.Loading(), LceState.Error(UnauthorizedException())),
            emitted.await()
        )
    }

    private inline fun test(crossinline block: suspend TestScope.(SessionManager) -> Unit) = runTest(UnconfinedTestDispatcher()) {
        block(SessionManagerImpl(sessionStorage, backgroundScope))
    }
}
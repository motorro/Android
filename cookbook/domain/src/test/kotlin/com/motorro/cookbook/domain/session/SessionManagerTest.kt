@file:OptIn(ExperimentalCoroutinesApi::class)

package com.motorro.cookbook.domain.session

import com.motorro.cookbook.domain.profile
import com.motorro.cookbook.domain.session
import com.motorro.cookbook.domain.session.data.Session
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.LinkedList

class SessionManagerTest {
    private lateinit var sessionStorage: SessionStorage
    private lateinit var userApi: UserApi
    private lateinit var sessionManager: SessionManager

    @Before
    fun init() {
        sessionStorage = mockk()
        userApi = mockk()
    }

    private inline fun managerTest(crossinline block: suspend TestScope.() -> Unit) = runTest(UnconfinedTestDispatcher()) {
        sessionManager = SessionManagerImpl(sessionStorage, userApi, backgroundScope)
        block()
    }

    @Test
    fun returnsStoredSession() = managerTest {
        val session = Session.None
        every { sessionStorage.session } returns flowOf(session)

        val sessionFlow = LinkedList<Session>()
        backgroundScope.launch {
            sessionManager.session.collect {
                sessionFlow.add(it)
            }
        }

        assertEquals(
            listOf(Session.Loading, Session.None),
            sessionFlow
        )
    }

    @Test
    fun logsUserIn() = managerTest {
        every { sessionStorage.session } returns flowOf(session)
        coEvery { userApi.getProfile(any(), any()) } returns profile
        coEvery { sessionStorage.update(any()) } just Runs

        val sessionFlow = LinkedList<Session>()
        backgroundScope.launch {
            sessionManager.session.collect {
                sessionFlow.add(it)
            }
        }

        assertEquals(
            Result.success(profile),
            sessionManager.login("test", "password")
        )

        coEvery { userApi.getProfile("test", "password") }
        coVerify { sessionStorage.update(session) }

        assertEquals(
            listOf(Session.Loading, session, Session.Loading, session),
            sessionFlow
        )
    }

    @Test
    fun logsUserOut() = managerTest {
        every { sessionStorage.session } returns flowOf(session)
        coEvery { sessionStorage.update(any()) } just Runs

        sessionManager.logout()

        coVerify { sessionStorage.update(Session.None) }
    }
}
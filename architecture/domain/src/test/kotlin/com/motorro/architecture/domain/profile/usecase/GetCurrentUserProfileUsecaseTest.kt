package com.motorro.architecture.domain.profile.usecase

import com.motorro.architecture.core.error.UnknownException
import com.motorro.architecture.core.lce.LceState
import com.motorro.architecture.domain.profile.PROFILE
import com.motorro.architecture.domain.profile.ProfileRepository
import com.motorro.architecture.domain.profile.error.NotRegisteredException
import com.motorro.architecture.domain.session.SESSION_DATA
import com.motorro.architecture.domain.session.SessionManager
import com.motorro.architecture.domain.session.data.Session
import com.motorro.architecture.domain.session.error.UnauthorizedException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class GetCurrentUserProfileUsecaseTest {
    private lateinit var sessionFlow: MutableStateFlow<Session>
    private lateinit var profileRepository: ProfileRepository
    private lateinit var usecase: GetCurrentUserProfileUsecase

    @Before
    fun init() {
        sessionFlow = MutableStateFlow(Session.Loading)
        val sessionManager = mockk<SessionManager> {
            every { this@mockk.session } returns sessionFlow
        }
        profileRepository = mockk()
        usecase = GetCurrentUserProfileUsecaseImpl(sessionManager, profileRepository)
    }

    @Test
    fun loadsProfile() = test {
        every { profileRepository.getProfile(any()) } returns flowOf(PROFILE)

        val emitted = backgroundScope.async {
            usecase().take(2).toList()
        }

        sessionFlow.emit(Session.Active(SESSION_DATA))

        assertEquals(
            listOf(LceState.Loading(), LceState.Content(PROFILE)),
            emitted.await()
        )

        verify { profileRepository.getProfile(SESSION_DATA.userId) }
    }

    @Test
    fun failsWithUnauthorizedIfNoActiveSession() = test {
        val emitted = backgroundScope.async {
            usecase().take(2).toList()
        }

        sessionFlow.emit(Session.None)

        assertEquals(
            listOf(LceState.Loading(), LceState.Error(UnauthorizedException())),
            emitted.await()
        )
    }

    @Test
    fun failsWithNotRegisteredIfThereIsNoProfile() = test {
        every { profileRepository.getProfile(any()) } returns flowOf(null)

        val emitted = backgroundScope.async {
            usecase().take(2).toList()
        }

        sessionFlow.emit(Session.Active(SESSION_DATA))

        assertEquals(
            listOf(LceState.Loading(), LceState.Error(NotRegisteredException())),
            emitted.await()
        )
    }

    @Test
    fun failsIfRepositoryFails() = test {
        val error = UnknownException(RuntimeException("Error"))
        every { profileRepository.getProfile(any()) } returns flow {
            throw error
        }

        val emitted = backgroundScope.async {
            usecase().take(2).toList()
        }

        sessionFlow.emit(Session.Active(SESSION_DATA))

        assertEquals(
            listOf(LceState.Loading(), LceState.Error(error)),
            emitted.await()
        )
    }

    private inline fun test(crossinline block: suspend TestScope.() -> Unit) = runTest(UnconfinedTestDispatcher()) {
        block()
    }
}
package com.motorro.architecture.domain.profile.usecase

import com.motorro.architecture.core.error.UnknownException
import com.motorro.architecture.core.lce.LceState
import com.motorro.architecture.domain.profile.NEW_PROFILE
import com.motorro.architecture.domain.profile.ProfileRepository
import com.motorro.architecture.domain.session.SESSION_DATA
import com.motorro.architecture.domain.session.SessionManager
import com.motorro.architecture.domain.session.data.Session
import com.motorro.architecture.domain.session.error.UnauthorizedException
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class UpdateCurrentUserProfileUsecaseTest {
    private lateinit var sessionFlow: MutableStateFlow<Session>
    private lateinit var profileRepository: ProfileRepository
    private lateinit var usecase: UpdateCurrentUserProfileUsecase

    @Before
    fun init() {
        sessionFlow = MutableStateFlow(Session.Loading)
        val sessionManager = mockk<SessionManager> {
            every { this@mockk.session } returns sessionFlow
        }
        profileRepository = mockk()
        usecase = UpdateCurrentUserProfileUsecaseImpl(sessionManager, profileRepository)
    }

    @Test
    fun updatesProfile() = test {
        coEvery { profileRepository.setProfile(any(), any()) } just Runs

        val emitted = backgroundScope.async {
            usecase(NEW_PROFILE).take(2).toList()
        }

        sessionFlow.emit(Session.Active(SESSION_DATA))

        assertEquals(
            listOf(LceState.Loading(), LceState.Content(Unit)),
            emitted.await()
        )

        coVerify { profileRepository.setProfile(SESSION_DATA.userId, NEW_PROFILE) }
    }

    @Test
    fun failsWithUnauthorizedIfNoActiveSession() = test {
        val emitted = backgroundScope.async {
            usecase(NEW_PROFILE).take(2).toList()
        }

        sessionFlow.emit(Session.None)

        assertEquals(
            listOf(LceState.Loading(), LceState.Error(UnauthorizedException())),
            emitted.await()
        )
    }

    @Test
    fun failsIfRepositoryFails() = test {
        val error = UnknownException(RuntimeException("Error"))
        coEvery { profileRepository.setProfile(any(), any()) } answers  {
            throw error
        }

        val emitted = backgroundScope.async {
            usecase(NEW_PROFILE).take(2).toList()
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
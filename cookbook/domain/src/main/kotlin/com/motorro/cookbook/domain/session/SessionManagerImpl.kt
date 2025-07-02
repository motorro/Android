package com.motorro.cookbook.domain.session

import com.motorro.cookbook.core.error.toCore
import com.motorro.cookbook.core.log.Logging
import com.motorro.cookbook.domain.session.data.Session
import com.motorro.cookbook.model.Profile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class SessionManagerImpl(
    private val sessionStorage: SessionStorage,
    private val userApi: UserApi,
    scope: CoroutineScope
) : SessionManager, Logging {

    private val loading = MutableStateFlow(false)

    override val session: StateFlow<Session> = loading.flatMapLatest { isLoading ->
        flow {
            if (isLoading) {
                emit(Session.Loading)
            } else {
                emitAll(sessionStorage.session)
            }
        }
    }.stateIn(scope, SharingStarted.Lazily, Session.Loading)

    override suspend fun login(username: String, password: String): Result<Profile> {
        d { "Logging in..." }

        loading.emit(true)

        val profile = try {
            userApi.getProfile(username, password)
        } catch (e: Throwable) {
            loading.emit(false)
            currentCoroutineContext().ensureActive()
            return Result.failure(e.toCore())
        }

        sessionStorage.update(
            Session.Active(
                username,
                password,
                profile
            )
        )

        loading.emit(false)

        d { "Logged in as ${profile.name}" }

        return Result.success(profile)
    }

    override suspend fun logout() {
        d { "Logging out..." }
        sessionStorage.update(Session.None)
    }
}

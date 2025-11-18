package com.motorro.cookbook.domain.session

import com.motorro.cookbook.core.error.toCore
import com.motorro.cookbook.core.log.Logging
import com.motorro.cookbook.domain.session.data.Session
import com.motorro.cookbook.model.Profile
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@OptIn(ExperimentalCoroutinesApi::class)
internal class SessionManagerImpl @Inject constructor(
    private val sessionStorage: SessionStorage,
    private val userApi: UserApi,
    private val userHandlers: @JvmSuppressWildcards Set<UserHandler>,
    @param:Named("Application") private val scope: CoroutineScope
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

        if (userHandlers.isNotEmpty()) {
            scope.launch(SupervisorJob() + CoroutineName("Session handling")) {
                userHandlers.forEach {
                    launch { it.onLoggedIn(profile) }
                }
            }
        }

        return Result.success(profile)
    }

    override suspend fun logout() {
        d { "Logging out..." }

        val currentProfile = (session.value as? Session.Active)?.profile

        sessionStorage.update(Session.None)

        if (userHandlers.isNotEmpty() && null != currentProfile) {
            scope.launch(SupervisorJob() + CoroutineName("Session handling")) {
                userHandlers.forEach {
                    launch { it.onLoggedOut(currentProfile) }
                }
            }
        }
    }
}

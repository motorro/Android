package com.motorro.cookbook.app.session

import android.util.Log
import com.motorro.cookbook.app.data.CookbookError
import com.motorro.cookbook.app.session.data.Session
import com.motorro.cookbook.data.Profile
import com.motorro.cookbook.data.UserId
import com.motorro.core.lce.LceState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

private const val TAG = "SessionManager"

/**
 * Session manager
 */
interface SessionManager {
    val session: StateFlow<Session>
    suspend fun login(username: String, password: String): Result<Profile>
    suspend fun logout()

    @OptIn(ExperimentalCoroutinesApi::class)
    class Impl(
        private val sessionStorage: SessionStorage,
        private val userApi: UserApi,
        scope: CoroutineScope,
        private val log: (String) -> Unit = { Log.d(TAG, it) }
    ) : SessionManager {

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
            log("Logging in...")

            loading.emit(true)

            val profile = try {
                userApi.getProfile(username, password)
            } catch (e: Throwable) {
                loading.emit(false)
                currentCoroutineContext().ensureActive()
                return Result.failure(e as? CookbookError ?: CookbookError.Unknown(e))
            }

            sessionStorage.update(Session.Active(
                username,
                password,
                profile
            ))

            loading.emit(false)

            log("Logged in as ${profile.name}")
            return Result.success(profile)
        }

        override suspend fun logout() {
            log("Logging out...")
            sessionStorage.update(Session.None)
        }
    }
}

/**
 * Checks if user is logged in
 */
suspend fun SessionManager.requireUserId(): UserId {
    val session = session.filter { it !is Session.Loading }.first()
    return when (session) {
        is Session.Active -> session.profile.id
        else -> throw CookbookError.Unauthorized("No active user")
    }
}

/**
 * Requires active user and returns new LCE flow created with this ID
 * @param block LCE flow generator
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <DATA: Any> SessionManager.withUserId(block: suspend (UserId) -> Flow<LceState<DATA, CookbookError>>): Flow<LceState<DATA, CookbookError>> = session.flatMapLatest { session ->
    when(session) {
        Session.Loading -> flowOf(LceState.Loading())
        is Session.Active -> flow {
            emitAll(block(session.profile.id))
        }
        Session.None -> flowOf(LceState.Error(CookbookError.Unauthorized("No active user")))
    }
}

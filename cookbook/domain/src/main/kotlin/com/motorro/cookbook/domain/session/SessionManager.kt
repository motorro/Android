package com.motorro.cookbook.domain.session

import com.motorro.cookbook.core.error.CoreException
import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.domain.session.data.Session
import com.motorro.cookbook.domain.session.error.UnauthorizedException
import com.motorro.cookbook.model.Profile
import com.motorro.cookbook.model.UserId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

private const val TAG = "SessionManager"

/**
 * Session manager
 */
interface SessionManager {
    /**
     * Current session
     */
    val session: StateFlow<Session>

    /**
     * Logs user in
     */
    suspend fun login(username: String, password: String): Result<Profile>

    /**
     * Logs user out
     */
    suspend fun logout()
}

/**
 * Gets actual session state
 */
suspend fun SessionManager.actualSession(): Session = session.filter { it !is Session.Loading }.first()

/**
 * Checks if user is logged in
 */
suspend fun SessionManager.requireUserId(): UserId {
    val session = actualSession()
    return when (session) {
        is Session.Active -> session.profile.id
        else -> throw UnauthorizedException()
    }
}

/**
 * Requires active user and returns new LCE flow created with this ID
 * @param block LCE flow generator
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <DATA: Any> SessionManager.withUserId(block: suspend (UserId) -> Flow<LceState<DATA, CoreException>>): Flow<LceState<DATA, CoreException>> = session.flatMapLatest { session ->
    when(session) {
        Session.Loading -> flowOf(LceState.Loading())
        is Session.Active -> flow {
            emitAll(block(session.profile.id))
        }
        Session.None -> flowOf(LceState.Error(UnauthorizedException()))
    }
}

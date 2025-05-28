package com.motorro.architecture.domain.session

import com.motorro.architecture.core.error.CoreException
import com.motorro.architecture.core.error.toCore
import com.motorro.architecture.core.lce.LceState
import com.motorro.architecture.domain.session.data.Session
import com.motorro.architecture.domain.session.data.SessionData
import com.motorro.architecture.domain.session.error.UnauthorizedException
import com.motorro.architecture.model.user.UserId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.transformLatest

/**
 * Session manager
 */
interface SessionManager {
    /**
     * Session state
     */
    val session: StateFlow<Session>

    /**
     * Activates session
     * @param session Valid session data
     */
    suspend fun login(session: SessionData)

    /**
     * Logs user out
     */
    suspend fun logout()
}

/**
 * Gets valid user ID if logged in
 */
suspend fun SessionManager.requireUserId(): UserId {
    val session = session.filter { it !is Session.Loading }.first()
    return when (session) {
        is Session.Active -> session.data.userId
        else -> throw UnauthorizedException()
    }
}

/**
 * Requires active user and returns new LCE flow created with this ID
 * @param block LCE flow generator
 */
@OptIn(ExperimentalCoroutinesApi::class)
inline fun <DATA: Any> SessionManager.flatMapUserId(crossinline block: suspend (UserId) -> Flow<LceState<DATA, CoreException>>): Flow<LceState<DATA, CoreException>> = session.flatMapLatest { session ->
    when(session) {
        Session.Loading -> flowOf(LceState.Loading())
        is Session.Active -> block(session.data.userId)
        Session.None -> flowOf(LceState.Error(UnauthorizedException()))
    }
}

/**
 * Requires active user and returns new LCE flow created with this ID
 * @param block LCE flow generator
 */
@OptIn(ExperimentalCoroutinesApi::class)
inline fun <DATA: Any> SessionManager.transformUserId(crossinline block: suspend FlowCollector<LceState<DATA, CoreException>>.(UserId) -> Unit): Flow<LceState<DATA, CoreException>> = session.transformLatest { session ->
    when(session) {
        Session.Loading -> emit(LceState.Loading())
        is Session.Active -> try { block(session.data.userId) } catch (e: Throwable) {
            currentCoroutineContext().ensureActive()
            emit(LceState.Error(e.toCore()))
        }
        Session.None -> emit(LceState.Error(UnauthorizedException()))
    }
}
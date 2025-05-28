package com.motorro.architecture.domain.session

import com.motorro.architecture.core.log.Logging
import com.motorro.architecture.domain.session.data.Session
import com.motorro.architecture.domain.session.data.SessionData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn

/**
 * [SessionManager] implementation
 */
internal class SessionManagerImpl(private val sessionStorage: SessionStorage, scope: CoroutineScope) : SessionManager, Logging {

    override val session: StateFlow<Session> = flowOf<Session>(Session.Loading)
        .onCompletion {
            emitAll(sessionStorage.session.map {
                if (null == it) Session.None else Session.Active(it)
            })
        }
        .stateIn(scope, SharingStarted.Lazily, Session.Loading)

    override suspend fun login(session: SessionData) {
        d { "Logging in..." }
        sessionStorage.update(session)
    }

    override suspend fun logout() {
        d { "Logging out..." }
        sessionStorage.update(null)
    }
}

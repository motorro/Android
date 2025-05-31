package com.motorro.architecture.session

import com.motorro.architecture.domain.session.SessionStorage
import com.motorro.architecture.domain.session.data.SessionData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

/**
 * Stores session in-memory
 */
class MemorySessionStorage(private val delay: Long, initialSession: SessionData? = null) : SessionStorage {

    private val sessionData = MutableStateFlow(initialSession)

    override val session: Flow<SessionData?> get() = flow {
        delay(delay)
        emitAll(sessionData.asStateFlow())
    }

    override suspend fun update(session: SessionData?) {
        delay(delay)
        sessionData.emit(session)
    }
}
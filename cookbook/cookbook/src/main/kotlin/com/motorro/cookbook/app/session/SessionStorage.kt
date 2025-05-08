package com.motorro.cookbook.app.session

import com.motorro.cookbook.app.session.data.Session
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Session storage
 */
interface SessionStorage {
    /**
     * Current session flow. Updates whenever session changes
     */
    val session: Flow<Session>

    /**
     * Call to update current session
     */
    suspend fun update(session: Session)
}

/**
 * Temporary memory solution
 */
class MemorySessionStorage : SessionStorage {
    private val value = MutableStateFlow<Session>(Session.None)

    override val session: Flow<Session> get() = value

    override suspend fun update(session: Session) {
        value.emit(session)
    }
}
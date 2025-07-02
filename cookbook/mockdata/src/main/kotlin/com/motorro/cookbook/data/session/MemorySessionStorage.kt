package com.motorro.cookbook.data.session

import com.motorro.cookbook.domain.session.SessionStorage
import com.motorro.cookbook.domain.session.data.Session
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Temporary memory solution
 */
class MemorySessionStorage(session: Session = Session.None) : SessionStorage {
    private val value = MutableStateFlow(session)

    override val session: Flow<Session> get() = value

    override suspend fun update(session: Session) {
        value.emit(session)
    }
}
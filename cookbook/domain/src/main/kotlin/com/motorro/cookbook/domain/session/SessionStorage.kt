package com.motorro.cookbook.domain.session

import com.motorro.cookbook.domain.session.data.Session
import kotlinx.coroutines.flow.Flow

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

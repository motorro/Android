package com.motorro.architecture.domain.session

import com.motorro.architecture.domain.session.data.SessionData
import kotlinx.coroutines.flow.Flow

/**
 * Session storage
 */
interface SessionStorage {
    /**
     * Current session flow. Updates whenever session changes
     */
    val session: Flow<SessionData?>

    /**
     * Call to update current session
     */
    suspend fun update(session: SessionData?)
}
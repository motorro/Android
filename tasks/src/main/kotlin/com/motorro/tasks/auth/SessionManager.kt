package com.motorro.tasks.auth

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.motorro.core.lce.LceState
import com.motorro.core.log.Logging
import com.motorro.tasks.auth.data.Session
import com.motorro.tasks.auth.data.SessionError
import com.motorro.tasks.auth.net.AuthApi
import com.motorro.tasks.data.AuthRequest
import com.motorro.tasks.data.ErrorCode
import com.motorro.tasks.data.HttpResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * Manages user session
 */
interface SessionManager {
    /**
     * Current session state
     */
    val session: StateFlow<LceState<Session, SessionError>>

    /**
     * Authenticates user
     * @param request Authentication request
     */
    fun authenticate(request: AuthRequest): Flow<LceState<Session.Active, SessionError>>

    /**
     * Logs current user out
     */
    fun logout(): Flow<LceState<Session.NONE, SessionError>>

    /**
     * Session manager implementation using
     * - Jetpack DataStore to save session locally
     * - Ktor client to authenticate on the backend
     * Note: this code stores authentication token as a readable text. Consider using secure storage in real apps.
     */
    class Impl(
        context: Context,
        scope: CoroutineScope,
        private val authApi: AuthApi,
        internal val dataStore: DataStore<Session> = context.sessionStore
    ) : SessionManager, Logging {

        /**
         * Current session state
         */
        override val session: StateFlow<LceState<Session, SessionError>> = dataStore.data
            .map<Session, LceState<Session, SessionError>> { LceState.Content(it) }
            .catch {
                emit(LceState.Error(SessionError.Storage(it)))
            }
            .stateIn(
                scope,
                started = SharingStarted.Lazily,
                LceState.Loading()
            )

        /**
         * Emits session result if successfully saved
         */
        private suspend fun <S: Session> FlowCollector<LceState<S, SessionError>>.emitIfSaved(session: S) {
            try {
                dataStore.updateData { session }
            } catch (e: Throwable) {
                currentCoroutineContext().ensureActive()
                w(e) { "Error writing session storage" }
                emit(LceState.Error(SessionError.Storage(e)))
                return
            }
            emit(LceState.Content(session))
        }

        /**
         * Authenticates user
         */
        override fun authenticate(request: AuthRequest): Flow<LceState<Session.Active, SessionError>> = flow {
            d { "Logging user in..." }
            emit(LceState.Loading())

            val response = try {
                authApi.login(request)
            } catch (e: Throwable) {
                currentCoroutineContext().ensureActive()
                w(e) { "Error running auth request" }
                emit(LceState.Error(SessionError.Network(e)))
                return@flow
            }

            when(response) {
                is HttpResponse.Data -> {
                    d { "Logged-in successfully" }
                    this.emitIfSaved(Session.Active(response.data))
                }
                is HttpResponse.Error -> {
                    w { "Authentication error: ${response.code}"}
                    when(response.code) {
                        ErrorCode.UNAUTHORIZED, ErrorCode.FORBIDDEN -> {
                            // User failed to authenticate - need an explicit return to the login form
                            emit(LceState.Error(SessionError.Authentication(response.code, response.message)))
                        }
                        else -> {
                            // In tested an working system, consider all errors as connectivity
                            emit(LceState.Error(SessionError.Network(IOException(response.message))))
                        }
                    }
                }
            }
        }

        /**
         * Logs current user out
         */
        override fun logout(): Flow<LceState<Session.NONE, SessionError>> = flow {
            d { "Logging user out..." }
            this.emitIfSaved(Session.NONE)
        }

        companion object {
            /**
             * Default file name for session storage
             */
            @VisibleForTesting
            const val DEFAULT_FILE_NAME = "session.pb"

            /**
             * Session serializer
             */
            @VisibleForTesting
            @OptIn(ExperimentalSerializationApi::class)
            internal val SessionSerializer = object : Serializer<Session> {
                override val defaultValue: Session = Session.NONE
                override suspend fun readFrom(input: InputStream): Session {
                    return Json.decodeFromStream(input)
                }
                override suspend fun writeTo(t: Session, output: OutputStream) {
                    Json.encodeToStream(t, output)
                }
            }

            private val Context.sessionStore: DataStore<Session> by dataStore(
                fileName = DEFAULT_FILE_NAME,
                serializer = SessionSerializer
            )
        }
    }
}
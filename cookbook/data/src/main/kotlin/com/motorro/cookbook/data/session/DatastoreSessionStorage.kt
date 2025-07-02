package com.motorro.cookbook.data.session

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import com.motorro.cookbook.domain.session.SessionStorage
import com.motorro.cookbook.domain.session.data.Session
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.InputStream
import java.io.OutputStream

/**
 * Datastore session storage implementation
 */
class DatastoreSessionStorage(private val context: Context): SessionStorage {
    override val session: Flow<Session> = context.sessionDataStore.data

    override suspend fun update(session: Session) {
        context.sessionDataStore.updateData { session }
    }
}

/**
 * Kotlin proto Data store for session
 */
private val Context.sessionDataStore: DataStore<Session> by dataStore(
    fileName = "session",
    serializer = SessionSerializer,
    corruptionHandler = ReplaceFileCorruptionHandler(
        produceNewData = { Session.None }
    )
)

/**
 * Session serializer
 */
@OptIn(ExperimentalSerializationApi::class)
private object SessionSerializer : Serializer<Session> {
    override val defaultValue: Session = Session.None

    override suspend fun readFrom(input: InputStream): Session = withContext(Dispatchers.IO) {
        try { Json.decodeFromStream(Session.serializer(), input) } catch (e: Throwable) {
            currentCoroutineContext().ensureActive()
            throw CorruptionException("Cannot read session cproto.", e)
        }
    }

    override suspend fun writeTo(t: Session, output: OutputStream) = withContext(Dispatchers.IO) {
        try { Json.encodeToStream(Session.serializer(), t, output) } catch (e: Throwable) {
            currentCoroutineContext().ensureActive()
            throw CorruptionException("Cannot write proto.", e)
        }
    }
}
package ru.merionet.tasks.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import ru.merionet.core.lce.LceState
import ru.merionet.tasks.auth.SessionManager.Impl.Companion.SessionSerializer
import ru.merionet.tasks.auth.data.Session
import ru.merionet.tasks.auth.data.SessionError
import ru.merionet.tasks.auth.net.AuthApi
import ru.merionet.tasks.data.AuthRequest
import ru.merionet.tasks.data.ErrorCode
import ru.merionet.tasks.data.HttpResponse
import ru.merionet.tasks.data.SessionClaims
import java.io.File
import java.io.IOException
import java.util.LinkedList
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue


@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class SessionManagerTest {

    private val request = AuthRequest(
        username = "user",
        password = "password"
    )
    private val claims = SessionClaims(
        username = "user",
        token = "token"
    )

    private val testContext: Context = ApplicationProvider.getApplicationContext()
    private val datastoreFile = File(testContext.filesDir, SessionManager.Impl.DEFAULT_FILE_NAME)
    private val testDispatcher = UnconfinedTestDispatcher()
    private val api: AuthApi = mockk()

    private fun TestScope.createDataStore(): DataStore<Session> = DataStoreFactory.create(
        serializer = SessionSerializer,
        scope = this.backgroundScope + Dispatchers.IO,
        produceFile = { datastoreFile }
    )

    private fun TestScope.createManager(dataStore: DataStore<Session> = createDataStore()): SessionManager.Impl {
        return SessionManager.Impl(
            context = testContext,
            scope = this.backgroundScope,
            authApi = api,
            dataStore = dataStore
        )
    }

    @After
    fun removeDatastore() {
        File(testContext.filesDir, SessionManager.Impl.DEFAULT_FILE_NAME).deleteRecursively()
    }

    @Test
    fun returnsEmptySessionWhenNoSessionSaved() = runTest(testDispatcher) {
        val manager = createManager()
        assertEquals(
            listOf<LceState<Session, SessionError>>(
                LceState.Loading(),
                LceState.Content(Session.NONE)
            ),
            manager.session.take(2).toList()
        )
    }

    @Test
    fun logsUserIn() = runTest(testDispatcher) {
        val manager = createManager()
        val emissions = LinkedList<LceState<Session, SessionError>>()
        val collector = launch {
            manager.session.take(3).collect(emissions::add)
        }

        coEvery { api.login(request) } returns HttpResponse.Data(claims)
        assertEquals(
            listOf<LceState<Session, SessionError>>(
                LceState.Loading(),
                LceState.Content(Session.Active(claims))
            ),
            manager.authenticate(request).toList()
        )

        collector.join()
        assertEquals(
            listOf<LceState<Session, SessionError>>(
                LceState.Loading(),
                LceState.Content(Session.NONE),
                LceState.Content(Session.Active(claims))
            ),
            emissions
        )
    }

    @Test
    fun savesSession() = runTest(testDispatcher) {
        val dataStore = createDataStore()
        var manager = createManager(dataStore)

        coEvery { api.login(request) } returns HttpResponse.Data(claims)
        manager.authenticate(request).toList()

        // Create new manager
        manager = createManager(dataStore)
        assertEquals(
            listOf<LceState<Session, SessionError>>(
                LceState.Loading(),
                LceState.Content(Session.Active(claims))
            ),
            manager.session.take(2).toList()
        )
    }

    @Test
    fun logsOutUser() = runTest {
        val manager = createManager()
        val emissions = LinkedList<LceState<Session, SessionError>>()
        val collector = launch {
            manager.session.take(4).collect {
                emissions.add(it)
            }
        }

        coEvery { api.login(request) } returns HttpResponse.Data(claims)
        assertEquals(
            listOf<LceState<Session, SessionError>>(
                LceState.Loading(),
                LceState.Content(Session.Active(claims))
            ),
            manager.authenticate(request).toList()
        )
        assertEquals(
            listOf<LceState<Session, SessionError>>(
                LceState.Content(Session.NONE)
            ),
            manager.logout().toList()
        )

        collector.join()
        assertEquals(
            listOf<LceState<Session, SessionError>>(
                LceState.Loading(),
                LceState.Content(Session.NONE),
                LceState.Content(Session.Active(claims)),
                LceState.Content(Session.NONE)
            ),
            emissions
        )
    }

    @Test
    fun ifLoginFailsReturnsFatalError() = runTest {
        val manager = createManager()

        coEvery { api.login(request) } returns HttpResponse.Error(ErrorCode.FORBIDDEN, "Invalid password")

        val (first, second) = manager.authenticate(request).toList()
        assertTrue { first is LceState.Loading }
        assertIs<LceState.Error<*,*>>(second)
        assertTrue { second.error is SessionError.Authentication }
    }

    @Test
    fun ifServerFailsReturnsRetriableError() = runTest {
        val manager = createManager()

        coEvery { api.login(request) } returns HttpResponse.Error(ErrorCode.UNKNOWN, "Unknown error")

        val (first, second) = manager.authenticate(request).toList()
        assertTrue { first is LceState.Loading }
        assertIs<LceState.Error<*,*>>(second)
        assertTrue { second.error is SessionError.Network }
    }

    @Test
    fun ifNetworkFailsReturnsRetriableError() = runTest {
        val manager = createManager()
        val error = IOException("Network error")

        coEvery { api.login(request) } throws IOException("Network error")

        val (first, second) = manager.authenticate(request).toList()
        assertTrue { first is LceState.Loading }
        assertIs<LceState.Error<*,*>>(second)
        assertTrue { second.error is SessionError.Network }
    }
}
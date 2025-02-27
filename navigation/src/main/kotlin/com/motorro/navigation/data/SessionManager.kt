package com.motorro.navigation.data

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.motorro.navigation.App
import kotlinx.serialization.json.Json
import kotlin.properties.ReadOnlyProperty

private const val USER_NAME = "user"
private const val PASSWORD = "password"

/**
 * Session manager
 */
interface SessionManager {
    fun getSession(): Session
    fun login(username: String, password: String): Session
    fun logout(): Session.NONE

    class Impl(context: Context, name: String) : SessionManager {
        private val prefs = context.getSharedPreferences(name, Context.MODE_PRIVATE)

        override fun getSession(): Session = prefs.getString(KEY_SESSION, null)
            ?.let { session -> Json.decodeFromString(Session.serializer(), session) }
            ?: Session.NONE

        override fun login(username: String, password: String): Session {
            if (username == USER_NAME && password == PASSWORD) {
                val session = Session.Active(username, System.currentTimeMillis())
                prefs.edit().putString(KEY_SESSION, Json.encodeToString(Session.serializer(), session)).apply()
                return session
            }
            throw IllegalArgumentException("Invalid credentials")
        }

        override fun logout(): Session.NONE {
            val session = Session.NONE
            prefs.edit().putString(KEY_SESSION, Json.encodeToString(Session.serializer(), session)).apply()
            return session
        }

        companion object {
            private const val KEY_SESSION = "session"
        }
    }
}

/**
 * Gets session manager from context
 */
private fun Context.getSessionManager(): SessionManager = (applicationContext as App).sessionManager

/**
 * Checks session state
 */
class SessionHelper(
    private val getContext: () -> Context,
    private val onAuthenticated: (Session.Active) -> Unit,
    private val onNonAuthenticated: () -> Unit
): DefaultLifecycleObserver {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(owner: LifecycleOwner) {
        sessionManager = getContext().getSessionManager()
    }

    override fun onStart(owner: LifecycleOwner) {
        when (val session = sessionManager.getSession()) {
            is Session.Active -> {
                onAuthenticated(session)
            }
            else -> {
                onNonAuthenticated()
            }
        }
    }

    fun logout() {
        sessionManager.logout()
        onNonAuthenticated()
    }
}

/**
 * Gets session helper from fragment
 */
private class SessionManagerContextDelegate: ReadOnlyProperty<Fragment, SessionManager> {
    override fun getValue(thisRef: Fragment, property: kotlin.reflect.KProperty<*>): SessionManager {
        return thisRef.requireContext().getSessionManager()
    }
}

/**
 * Gets session helper from fragment
 */
fun sessionManager(): ReadOnlyProperty<Fragment, SessionManager> = SessionManagerContextDelegate()

/**
 * Gets session helper from fragment
 */
private class SessionHelperFragmentDelegate(
    private val fragment: Fragment,
    onAuthenticated: (Session.Active) -> Unit,
    onNonAuthenticated: () -> Unit
): ReadOnlyProperty<Fragment, SessionHelper> {

    private var sessionHelper: SessionHelper = SessionHelper(
        getContext = { fragment.requireContext() },
        onAuthenticated = onAuthenticated,
        onNonAuthenticated = onNonAuthenticated
    )

    init {
        fragment.lifecycle.addObserver(sessionHelper)
    }

    override fun getValue(thisRef: Fragment, property: kotlin.reflect.KProperty<*>): SessionHelper {
        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED).not()) {
            throw IllegalStateException("Should not attempt to get session manager before fragment is created.")
        }

        return sessionHelper
    }
}

/**
 * Gets session helper from fragment
 */
fun Fragment.sessionHelper(
    onAuthenticated: (Session.Active) -> Unit,
    onNonAuthenticated: () -> Unit
): ReadOnlyProperty<Fragment, SessionHelper> = SessionHelperFragmentDelegate(
    fragment = this,
    onAuthenticated = onAuthenticated,
    onNonAuthenticated = onNonAuthenticated
)


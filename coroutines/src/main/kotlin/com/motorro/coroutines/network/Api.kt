package com.motorro.coroutines.network

import android.os.Handler
import android.os.Looper
import android.os.NetworkOnMainThreadException
import android.util.Log
import com.motorro.coroutines.network.data.LoginRequest
import com.motorro.coroutines.network.data.Profile
import com.motorro.coroutines.network.data.User
import kotlin.concurrent.thread
import kotlin.time.Instant

private const val NETWORK_DELAY = 1000L

private const val LOGIN = "login"
private const val PASSWORD = "password"

private val LOGIN_RESPONSE = User(1, "token")
private val PROFILE = Profile(
    1,
    "Ivan Petrov",
    30,
    Instant.parse("2021-01-01T00:00:00Z")
)

/**
 * Server API
 */
interface Api {
    /**
     * Login
     * @param credentials Login credentials
     */
    fun login(credentials: LoginRequest, willFail: Boolean = false, onResult: (Result<User>) -> Unit)

    /**
     * Retrieves profile
     * @param token User token
     * @param id Profile ID
     */
    fun getProfile(token: String, id: Long, willFail: Boolean = false, onResult: (Result<Profile>) -> Unit)

    companion object {
        /**
         * Creates API service
         */
        fun create(): Api = object : Api {
            override fun login(credentials: LoginRequest, willFail: Boolean, onResult: (Result<User>) -> Unit) = runInBackground(onResult) {
                emulateNetwork {
                    log { "Logging in ${credentials.username}..." }
                    if (willFail) {
                        throw RuntimeException("Login failed")
                    }
                    if (LOGIN == credentials.username  && PASSWORD == credentials.password) {
                        LOGIN_RESPONSE
                    } else {
                        throw IllegalArgumentException("Invalid credentials")
                    }
                }
            }

            override fun getProfile(token: String, id: Long, willFail: Boolean, onResult: (Result<Profile>) -> Unit) = runInBackground(onResult) {
                emulateNetwork {
                    log { "Getting profile for $id..." }
                    if (willFail) {
                        throw RuntimeException("Profile load failed")
                    }
                    when {
                        LOGIN_RESPONSE.token != token -> throw IllegalArgumentException("Invalid token")
                        PROFILE.id != id -> throw IllegalStateException("Unknown profile")
                        else -> PROFILE
                    }
                }
            }
        }
    }
}

private fun <T> runInBackground(callback: (Result<T>) -> Unit, block: () -> Result<T>) {
    thread {
        log { "Switched to background thread..." }
        val result = block()
        Handler(Looper.getMainLooper()).post {
            log { "Switched to main thread..." }
            callback(result)
        }
    }
}

private inline fun <T> emulateNetwork(block: () -> T): Result<T> = try {
    log { "Emulating network..." }
    val result = block()
    if (Looper.getMainLooper().isCurrentThread) {
        throw NetworkOnMainThreadException()
    }
    Thread.sleep(NETWORK_DELAY)
    Result.success(result)
} catch (e: Exception) {
    Result.failure(e)
}

private fun log(block: () -> String) {
    val threadName = Thread.currentThread().name
    Log.i("API", "===== log =====")
    Log.i("API", "Thread: $threadName")
    Log.i("API", block())
}
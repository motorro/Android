package com.motorro.coroutines.network

import android.os.Looper
import android.os.NetworkOnMainThreadException
import android.util.Log
import com.motorro.coroutines.network.data.LoginRequest
import com.motorro.coroutines.network.data.Profile
import com.motorro.coroutines.network.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
    suspend fun login(credentials: LoginRequest, willFail: Boolean = false): User

    /**
     * Retrieves profile
     * @param token User token
     * @param id Profile ID
     */
    suspend fun getProfile(token: String, id: Long, willFail: Boolean = false): Profile

    companion object {
        /**
         * Creates API service
         */
        fun create(): Api = object : Api {
            override suspend fun login(credentials: LoginRequest, willFail: Boolean): User = runInBackground {
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

            override suspend fun getProfile(token: String, id: Long, willFail: Boolean): Profile = runInBackground {
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

private suspend inline fun <T> runInBackground(crossinline block: () -> Result<T>): T {
    log { "Starting background run..." }
    val result = withContext(Dispatchers.IO) {
        log { "Switched to background thread..." }
        block().getOrThrow()
    }
    log { "Back on original context..." }
    return result
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
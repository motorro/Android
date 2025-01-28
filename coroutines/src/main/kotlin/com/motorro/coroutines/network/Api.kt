package com.motorro.coroutines.network

import android.os.Looper
import android.os.NetworkOnMainThreadException
import android.util.Log
import com.motorro.coroutines.network.data.LoginRequest
import com.motorro.coroutines.network.data.Profile
import com.motorro.coroutines.network.data.User
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
    fun login(credentials: LoginRequest): Result<User>

    /**
     * Retrieves profile
     * @param token User token
     * @param id Profile ID
     */
    fun getProfile(token: String, id: Long): Result<Profile>

    companion object {
        /**
         * Creates API service
         */
        fun create(): Api = object : Api {
            override fun login(credentials: LoginRequest): Result<User> = emulateNetwork {
                log { "Logging in ${credentials.username}..." }
                if (LOGIN == credentials.username  && PASSWORD == credentials.password) {
                    LOGIN_RESPONSE
                } else {
                    throw IllegalArgumentException("Invalid credentials")
                }
            }

            override fun getProfile(token: String, id: Long): Result<Profile> = emulateNetwork {
                log { "Getting profile for $id..." }
                when {
                    LOGIN_RESPONSE.token != token -> throw IllegalArgumentException("Invalid token")
                    PROFILE.id != id -> throw IllegalStateException("Unknown profile")
                    else -> PROFILE
                }
            }
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
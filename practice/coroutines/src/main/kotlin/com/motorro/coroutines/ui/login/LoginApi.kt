package com.motorro.coroutines.ui.login

import android.os.Looper
import android.os.NetworkOnMainThreadException
import android.util.Log
import com.motorro.coroutines.ui.login.data.Credentials
import com.motorro.coroutines.ui.login.data.User

class LoginApi {
    fun login(credentials: Credentials): User = emulateNetworkRequest {
        if (credentials.login == "admin" && credentials.password == "password") {
            User(1, "Admin")
        } else {
            throw IllegalArgumentException("Invalid credentials")
        }
    }

    fun logout() = emulateNetworkRequest {
        Unit
    }

    private inline fun <T> emulateNetworkRequest(crossinline block: () -> T): T {
        Log.i(TAG, "emulateNetworkRequest: running on ${Thread.currentThread().name}")
        if(Looper.getMainLooper().thread == Thread.currentThread()) {
            throw NetworkOnMainThreadException()
        }
        Thread.sleep(NETWORK_DELAY)
        return block()
    }

    private companion object {
        private const val TAG = "NetworkApi"
        private const val NETWORK_DELAY = 1000L
    }
}
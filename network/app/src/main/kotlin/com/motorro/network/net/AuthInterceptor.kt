package com.motorro.network.net

import com.motorro.network.session.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        // We are in a synchronous context, so we need
        // to block the thread to get the token (illustration)
        val token = runBlocking {
            sessionManager.token.first()
        }

        if (token != null) {
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        }

        return chain.proceed(request)
    }
}
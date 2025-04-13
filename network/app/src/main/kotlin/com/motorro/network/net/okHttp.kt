package com.motorro.network.net

import com.motorro.network.session.SessionManager
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Creates an instance of [OkHttpClient] with a custom interceptor.
 */
fun createAppHttpClient(sessionManager: SessionManager): OkHttpClient {
    return OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(AuthInterceptor(sessionManager))
        .addInterceptor(LoggingInterceptor())
        .build()
}
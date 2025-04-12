package com.motorro.network.net

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Creates an instance of [OkHttpClient] with a custom interceptor.
 */
fun createAppHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS)
        .build()
}
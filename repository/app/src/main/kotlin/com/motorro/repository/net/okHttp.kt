package com.motorro.repository.net

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * Creates an instance of [OkHttpClient]
 */
fun createAppHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {level = HttpLoggingInterceptor.Level.BASIC })
        .build()
}
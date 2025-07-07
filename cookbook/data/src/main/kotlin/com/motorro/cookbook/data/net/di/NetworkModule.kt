package com.motorro.cookbook.data.net.di

import com.motorro.cookbook.data.net.Config
import com.motorro.cookbook.data.net.ktorHttp
import com.motorro.cookbook.data.net.lenientJson
import com.motorro.cookbook.data.net.okhttp
import com.motorro.cookbook.data.net.retrofit
import com.motorro.cookbook.domain.session.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    /**
     * Json instance
     */
    @Provides
    fun json(): Json = lenientJson()

    /**
     * OkHttp client
     */
    @Provides
    @Singleton
    fun okHttpClient(): OkHttpClient = okhttp()

    /**
     * Authentication retrofit
     */
    @Provides
    fun retrofit(): Retrofit = retrofit(
        okHttpClient(),
        json(),
        Config.getBaseUrl()
    )

    /**
     * Ktor HTTP client
     */
    @Provides
    fun httpClient(okHttpClient: OkHttpClient, json: Json, sessionManager: SessionManager): HttpClient = ktorHttp(
        okHttpClient,
        json,
        sessionManager
    )
}
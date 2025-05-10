package com.motorro.cookbook.app

import android.app.Application
import com.motorro.cookbook.app.net.Config
import com.motorro.cookbook.app.net.lenientJson
import com.motorro.cookbook.app.net.okhttp
import com.motorro.cookbook.app.net.retrofit
import com.motorro.cookbook.app.repository.MockRepository
import com.motorro.cookbook.app.repository.RecipeRepository
import com.motorro.cookbook.app.session.MemorySessionStorage
import com.motorro.cookbook.app.session.MockUserApi
import com.motorro.cookbook.app.session.SessionManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * The application. Provides access to dependencies.
 */
@OptIn(DelicateCoroutinesApi::class)
@Suppress("MemberVisibilityCanBePrivate", "unused")
class App : Application() {

    /**
     * Json instance
     */
    fun json(): Json = lenientJson()

    /**
     * OkHttp client
     */
    fun okHttpClient(): OkHttpClient = okhttp()

    /**
     * Authentication retrofit
     */
    val authRetrofit: Retrofit by lazy {
        retrofit(okHttpClient(), json(), Config.getBaseUrl())
    }

    /**
     * Session manager
     */
    val sessionManager: SessionManager by lazy {
        SessionManager.Impl(MemorySessionStorage(), MockUserApi(), GlobalScope)
    }

    /**
     * The recipe repository.
     */
    val recipeRepository: RecipeRepository by lazy {
        MockRepository(sessionManager)
    }
}
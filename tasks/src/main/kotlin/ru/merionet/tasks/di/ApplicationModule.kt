package ru.merionet.tasks.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import ru.merionet.core.coroutines.DispatcherProvider
import ru.merionet.tasks.auth.SessionManager
import ru.merionet.tasks.data.httpResponseModule
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class App

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class LoginHttp

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class AppHttp

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Provides
    @Singleton
    fun dispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher = Dispatchers.Main
        override val default: CoroutineDispatcher = Dispatchers.Default
        override val io: CoroutineDispatcher = Dispatchers.IO
    }

    @App
    @Provides
    @Singleton
    fun applicationScope(): CoroutineScope = CoroutineScope(
        Dispatchers.Default + SupervisorJob()
    )

    @LoginHttp
    @Provides
    @Singleton
    fun loginHttpClient(): HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json { serializersModule = httpResponseModule })
        }
    }

    @AppHttp
    @Provides
    @Singleton
    fun appHttpClient(sessionManager: SessionManager): HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json { serializersModule = httpResponseModule })
        }
        install(Auth) {
            // Bearer token does not expire for this demo
            // But you could automatically refresh it here
            // See: https://ktor.io/docs/client-bearer-auth.html
            bearer {
                loadTokens {
                    sessionManager.getTokens().run {
                        BearerTokens(access, refresh)
                    }
                }
            }
        }
    }
}
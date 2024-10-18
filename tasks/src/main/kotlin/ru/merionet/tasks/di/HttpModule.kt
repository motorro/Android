package ru.merionet.tasks.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ru.merionet.tasks.app.net.TasksApi
import ru.merionet.tasks.auth.SessionManager
import ru.merionet.tasks.data.httpResponseModule
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class LoginHttp

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class AppHttp

/**
 * HTTP clients
 */
@Module
@InstallIn(SingletonComponent::class)
class HttpModule {

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

/**
 * Network APIs
 */
@Module
@InstallIn(ViewModelComponent::class)
interface NetworkModule {
    @Binds
    fun tasksApi(impl: TasksApi.Impl): TasksApi
}


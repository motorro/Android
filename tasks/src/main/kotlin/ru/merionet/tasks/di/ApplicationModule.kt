package ru.merionet.tasks.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import ru.merionet.core.coroutines.DispatcherProvider
import ru.merionet.tasks.data.httpResponseModule
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class App

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

    @Provides
    @Singleton
    fun httpClient(): HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json { serializersModule = httpResponseModule })
        }
    }
}
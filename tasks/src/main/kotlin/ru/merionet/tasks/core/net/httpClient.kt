package ru.merionet.tasks.core.net

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ru.merionet.tasks.data.httpResponseModule

/**
 * Creates HTTP client
 */
fun getHttpClient() = HttpClient {
    install(ContentNegotiation) {
        json(Json { serializersModule = httpResponseModule })
    }
}
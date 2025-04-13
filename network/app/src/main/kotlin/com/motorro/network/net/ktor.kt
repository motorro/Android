package com.motorro.network.net

import com.motorro.network.session.SessionManager
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.plugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

fun ktorAppHttpClient(sessionManager: SessionManager, json: Json): HttpClient {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
    }

    client.plugin(HttpSend).intercept { request: HttpRequestBuilder ->
        val token = sessionManager.token.first()
        if (null != token) {
            request.bearerAuth(token)
        }
        execute(request)
    }

    return client
}

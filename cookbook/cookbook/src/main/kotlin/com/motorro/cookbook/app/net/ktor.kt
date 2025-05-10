package com.motorro.cookbook.app.net

import com.motorro.cookbook.app.data.CookbookError.Unauthorized
import com.motorro.cookbook.app.data.CookbookError.Unknown
import com.motorro.cookbook.app.data.toCookbookError
import com.motorro.cookbook.app.session.SessionManager
import com.motorro.cookbook.app.session.data.Session
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient

/**
 * Provides Ktor HTTP client
 */
fun ktorHttp(okHttp: OkHttpClient, json: Json, sessionManager: SessionManager): HttpClient = HttpClient(OkHttp) {
    // Configure HTTP client
    engine {
        preconfigured = okHttp
    }

    // Content handling
    install(ContentNegotiation) {
        json(json)
    }

    // Error handling
    expectSuccess = true
    HttpResponseValidator {
        handleResponseExceptionWithRequest { e, _ ->
            when {
                e is ClientRequestException && Unauthorized == e.response.status -> Unauthorized(e.message)
                else -> Unknown(e)
            }
        }
    }

    // Authentication
    install(Auth) {
        basic {
            credentials {
                sessionManager.session.filterIsInstance<Session.Active>().first().let {
                    BasicAuthCredentials(username = it.username, password = it.password)
                }
            }
            sendWithoutRequest { true }
        }
    }
}

/**
 * Runs HTTP request returning body and checks for error
 */
suspend inline fun <reified T> request(block: () -> HttpResponse): Result<T> = runCatching {
    block().body<T>()
}.recoverCatching {
    currentCoroutineContext().ensureActive()
    throw it.toCookbookError()
}
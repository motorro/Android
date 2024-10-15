package ru.merionet.tasks.auth.net

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.withContext
import ru.merionet.core.coroutines.DispatcherProvider
import ru.merionet.tasks.core.net.platformUrl
import ru.merionet.tasks.data.AuthRequest
import ru.merionet.tasks.data.HttpResponse
import ru.merionet.tasks.data.SessionClaims
import ru.merionet.tasks.di.LoginHttp
import javax.inject.Inject

/**
 * Authentication API
 */
interface AuthApi {
    suspend fun login(credentials: AuthRequest): HttpResponse<SessionClaims>

    /**
     * Ktor API implementation
     */
    class Impl @Inject constructor(
        @LoginHttp private val httpClient: HttpClient,
        private val dispatchers: DispatcherProvider
    ) : AuthApi {
        override suspend fun login(credentials: AuthRequest): HttpResponse<SessionClaims> {
            val result = withContext(dispatchers.io) {
                httpClient.post {
                    platformUrl(listOf("login"))
                    contentType(ContentType.Application.Json)
                    setBody(credentials)
                }
            }
            return result.body()
        }
    }
}
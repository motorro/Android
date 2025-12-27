package com.motorro.tasks.auth.net

import com.motorro.core.coroutines.DispatcherProvider
import com.motorro.tasks.core.net.platformUrl
import com.motorro.tasks.data.AuthRequest
import com.motorro.tasks.data.HttpResponse
import com.motorro.tasks.data.SessionClaims
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.withContext
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
        private val httpClient: HttpClient,
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
package com.motorro.tasks.auth.net

import com.motorro.tasks.core.net.getHttpClient
import com.motorro.tasks.core.net.platformUrl
import com.motorro.tasks.data.AuthRequest
import com.motorro.tasks.data.HttpResponse
import com.motorro.tasks.data.SessionClaims
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface AuthApi {
    suspend fun login(credentials: AuthRequest): HttpResponse<SessionClaims>
}

class AuthApiImpl : AuthApi {
    private val httpClient = getHttpClient()

    override suspend fun login(credentials: AuthRequest): HttpResponse<SessionClaims> {
        val result = withContext(Dispatchers.IO) {
            httpClient.post {
                platformUrl(listOf("login"))
                contentType(ContentType.Application.Json)
                setBody(credentials)
            }
        }
        return result.body()
    }
}
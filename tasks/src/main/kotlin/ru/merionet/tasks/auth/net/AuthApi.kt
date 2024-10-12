package ru.merionet.tasks.auth.net

import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.merionet.tasks.core.net.getHttpClient
import ru.merionet.tasks.core.net.platformUrl
import ru.merionet.tasks.data.AuthRequest
import ru.merionet.tasks.data.HttpResponse
import ru.merionet.tasks.data.SessionClaims

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
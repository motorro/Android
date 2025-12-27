package com.motorro.tasks.server

import com.motorro.tasks.data.AuthRequest
import com.motorro.tasks.data.HttpResponse
import com.motorro.tasks.data.SessionClaims
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.bearer
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext
import org.jetbrains.annotations.VisibleForTesting

@VisibleForTesting
internal const val TOKEN = "token123"
@VisibleForTesting
internal const val USERNAME = "username"
@VisibleForTesting
internal const val PASSWORD = "password"

fun AuthenticationConfig.stubBearer(name: String? = null) {
    bearer(name) {
        realm = name
        authenticate { credential ->
            if (TOKEN == credential.token) {
                UserIdPrincipal(USERNAME)
            } else {
                null
            }
        }
    }
}

suspend fun RoutingContext.login() {
    val request = call.receiveNullable<AuthRequest>() ?: throw IllegalArgumentException("Login Request is invalid")
    if (USERNAME == request.username && PASSWORD == request.password) {
        call.respond<HttpResponse<SessionClaims>>(HttpResponse.Data(SessionClaims(request.username, TOKEN)))
    } else {
        call.respond(HttpStatusCode.Forbidden)
    }
}
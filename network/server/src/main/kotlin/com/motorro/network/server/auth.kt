package com.motorro.network.server

import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.bearer


fun AuthenticationConfig.stubBearer(name: String? = null) {
    bearer(name) {
        realm = name
        authenticate { credential ->
            if (TOKEN == credential.token) {
                UserIdPrincipal(ADMIN)
            } else {
                null
            }
        }
    }
}
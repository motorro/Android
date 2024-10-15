package ru.merionet.tasks.auth.data

import ru.merionet.tasks.data.SessionClaims

/**
 * Tokens for authentication
 * @property access Access token
 * @property refresh Refresh token
 */
data class Tokens(val access: String, val refresh: String)

/**
 * Band-aid for access/refresh tokens for current session
 */
internal fun SessionClaims.asTokens() = Tokens(token, token)
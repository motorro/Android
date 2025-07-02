package com.motorro.cookbook.server

import com.motorro.cookbook.model.Profile
import com.motorro.cookbook.model.UserId
import com.motorro.cookbook.server.db.tables.UserEntity
import com.motorro.cookbook.server.db.tables.UsersTable
import io.ktor.server.application.log
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.basic
import io.ktor.server.plugins.NotFoundException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.v1.core.eq

/**
 * User principal
 */
data class UserIdPrincipal(val id: UserId)

/**
 * User access
 */
interface User {
    /**
     * Authentication
     */
    fun auth(authenticationConfig: AuthenticationConfig, area: String)

    /**
     * Profile
     */
    suspend fun getProfile(id: UserId): Profile

    class Impl(private val context: CoroutineDispatcher = Dispatchers.IO) : User {
        override fun auth(authenticationConfig: AuthenticationConfig, area: String) {
            authenticationConfig.basic(area) {
                this.realm = "Recipes Server"
                this.validate { credentials ->
                    val user = tQuery {
                        UserEntity.find { UsersTable.name eq credentials.name }.firstOrNull()
                    }

                    if (null != user && credentials.password == user.password) {
                        this.application.log.info("User ${credentials.name} authenticated")
                        UserIdPrincipal(UserId(user.id.value))
                    } else {
                        this.application.log.warn("User ${credentials.name} authentication failed")
                        null
                    }
                }
            }
        }

        override suspend fun getProfile(id: UserId): Profile = tQuery {
            val user = UserEntity.findById(id.id) ?: throw NotFoundException("User not found")
            return@tQuery Profile(UserId(user.id.value), user.name)
        }
    }
}
package com.motorro.network.net.usecase

import com.motorro.network.data.User
import com.motorro.network.data.UserListItem
import com.motorro.network.net.UserApi
import com.motorro.network.session.SessionManager

/**
 * Use case to get a list of users.
 */
interface GetUserList {
    /**
     * Invokes the use case to get a list of users.
     *
     * @return A list of [UserListItem] representing the users.
     */
    suspend operator fun invoke(): Result<List<UserListItem>>

    class Impl(private val userApi: UserApi, private val sessionManager: SessionManager) : GetUserList {
        override suspend fun invoke(): Result<List<UserListItem>> {
            return userApi.getUserList().mapCatching { mapToUserListItem(it) }
        }

        private fun mapToUserListItem(users: List<User>): List<UserListItem> {
            val deleteEnabled = sessionManager.loggedIn.value
            return users.map { user ->
                UserListItem(user, deleteEnabled)
            }
        }
    }
}
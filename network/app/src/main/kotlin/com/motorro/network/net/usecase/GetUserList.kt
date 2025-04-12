package com.motorro.network.net.usecase

import com.motorro.network.data.User
import com.motorro.network.data.UserListItem
import com.motorro.network.net.UserApi

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

    class Impl(private val userApi: UserApi) : GetUserList {
        override suspend fun invoke(): Result<List<UserListItem>> {
            return userApi.getUserList().mapCatching { mapToUserListItem(it) }
        }

        private fun mapToUserListItem(users: List<User>): List<UserListItem> = users.map { user ->
            UserListItem(user, false)
        }
    }
}
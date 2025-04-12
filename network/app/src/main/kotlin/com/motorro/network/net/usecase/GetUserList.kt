package com.motorro.network.net.usecase

import com.motorro.network.data.User
import com.motorro.network.data.UserListItem
import com.motorro.network.net.UserApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import java.io.IOException

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
            val response = try {
                userApi.getUserList()
            } catch (e: IOException) {
                currentCoroutineContext().ensureActive()
                return Result.failure(e)
            }

            if (response.isSuccessful.not()) {
                return Result.failure(
                    IOException("Unexpected code: ${response.code()}, Body: ${response.errorBody()?.string()}")
                )
            }

            val users = response.body() ?: return Result.failure(
                IOException("Response body is null")
            )

            return Result.success(mapToUserListItem(users))
        }

        private fun mapToUserListItem(users: List<User>): List<UserListItem> = users.map { user ->
            UserListItem(user, false)
        }
    }
}
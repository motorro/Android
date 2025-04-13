package com.motorro.network.net.usecase

import com.motorro.network.net.UserApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

/**
 * Use case to delete a user.
 */
interface DeleteUser {
    /**
     * Invokes the use case to delete a user.
     *
     * @param userId The ID of the user to delete.
     */
    suspend operator fun invoke(userId: Int): Result<Unit>

    class Impl(private val userApi: UserApi) : DeleteUser {
        override suspend fun invoke(userId: Int): Result<Unit> {
            return try {
                Result.success(userApi.deleteUser(userId))
            } catch (e: Exception) {
                currentCoroutineContext().ensureActive()
                Result.failure(e)
            }
        }
    }
}
package com.motorro.network.net

import com.motorro.network.data.User
import retrofit2.http.GET

/**
 * Retrofit API for user data
 */
interface UserApi {
    /**
     * Returns a list of users
     */
    @GET("users")
    suspend fun getUserList(): Result<List<User>>
}
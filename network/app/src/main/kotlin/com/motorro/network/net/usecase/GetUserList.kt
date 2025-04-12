package com.motorro.network.net.usecase

import com.motorro.network.data.User
import com.motorro.network.data.UserListItem
import com.motorro.network.net.Config
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import okhttp3.OkHttpClient
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

    @OptIn(ExperimentalSerializationApi::class)
    class Impl(private val okHttpClient: OkHttpClient, private val json: Json) : GetUserList {
        override suspend fun invoke(): Result<List<UserListItem>> = withContext(Dispatchers.IO) {
            val request = okhttp3.Request.Builder()
                .get()
                .url(Config.getBaseUrl().newBuilder().addPathSegment("users").build())
                .build()

            runCatching {
                okHttpClient.newCall(request).execute().use { response ->
                    if (response.isSuccessful.not()) {
                        throw IOException("Unexpected code ${response.code}")
                    }
                    val users = json.decodeFromStream(
                        ListSerializer(User.serializer()),
                        response.body.byteStream()
                    )

                    return@runCatching mapToUserListItem(users)
                }
            }
        }

        private fun mapToUserListItem(users: List<User>): List<UserListItem> = users.map { user ->
            UserListItem(user, false)
        }
    }
}
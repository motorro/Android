package com.motorro.background.pages.blog.repository

import com.github.javafaker.Faker
import com.motorro.background.Constants
import com.motorro.background.pages.blog.repository.data.Post
import com.motorro.background.pages.blog.repository.data.PostList
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.toKotlinInstant

/**
 * Network API
 */
interface BlogApi {
    /**
     * Returns the latest [count] posts.
     */
    suspend fun getLatestPosts(count: Int = 3): PostList

    class Impl @Inject constructor(private val clock: Clock) : BlogApi {

        private val faker: Faker = Faker()

        override suspend fun getLatestPosts(count: Int): PostList {
            delay(Constants.SERVER_DELAY)
            val posts = (1..count).map {
                Post(
                    id = faker.internet().uuid(),
                    authorName = faker.funnyName().name(),
                    title = faker.hacker().noun(),
                    published = faker.date().past(10, TimeUnit.MINUTES).toInstant().toKotlinInstant(),
                )
            }
            return PostList(posts, clock.now())
        }
    }
}

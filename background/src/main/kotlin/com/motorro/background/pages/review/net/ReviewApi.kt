package com.motorro.background.pages.review.net

import android.content.Context
import androidx.core.net.toUri
import com.github.javafaker.Faker
import com.motorro.background.Constants
import com.motorro.background.pages.review.data.Photo
import com.motorro.background.pages.review.data.ReviewData
import com.motorro.core.log.Logging
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.random.Random
import kotlin.time.Duration

/**
 * Review upload API
 */
interface ReviewApi {
    /**
     * Uploads picture and returns resulting URI
     */
    suspend fun uploadPhoto(photo: Photo): Photo

    /**
     * Uploads review
     */
    suspend fun uploadReview(review: ReviewData)

    class Impl @Inject constructor(private val context: Context) : ReviewApi, Logging {

        private val faker: Faker = Faker()

        override suspend fun uploadPhoto(photo: Photo): Photo {
            val size = context.contentResolver.openAssetFileDescriptor(photo.uri, "r").use {
                it?.length ?: 0
            }
            i { "---------- Upload photo -------------" }
            i { "Uri: ${photo.uri}" }
            i { "Size: $size" }
            i { "--------------------------------------" }

            return withFailure(Constants.SERVER_DELAY) {
                val resultUri = "https://motorro.com/static/${faker.internet().uuid()}.jpg".toUri()
                i { "Result: $resultUri" }
                i { "--------------------------------------" }
                Photo(resultUri)
            }
        }

        override suspend fun uploadReview(review: ReviewData) {
            i { "---------- Upload review -------------" }
            i { "$review" }
            i { "--------------------------------------" }

            withFailure(Constants.SERVER_DELAY) {
                i { "Done!" }
                i { "--------------------------------------" }
            }
        }

        private suspend fun <T> withFailure(time: Duration, block: suspend () -> T): T {
            delay(time)
            val willFail = Random.nextInt(10) <= 4
            if (willFail) {
                w { "Upload failed" }
                throw Exception("Upload failed")
            }
            return block()
        }
    }
}
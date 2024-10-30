package com.motorro.activity.data

import com.github.javafaker.Faker
import java.util.concurrent.TimeUnit
import kotlin.time.Instant

private val faker = Faker.instance()

/**
 * Email data
 */
data class Email(
    val id: Int,
    val address: String,
    val subject: String,
    val body: String,
    val created: Instant
) {
    fun isValid() = address.isNotBlank() && subject.isNotBlank() && body.isNotBlank()
}

/**
 * List of emails
 */
val mailList = (0..20)
    .map {
        Email(
            id = it,
            address = faker.internet().emailAddress(),
            subject = faker.lorem().sentence(),
            body = faker.lorem().paragraph(),
            created = Instant.fromEpochMilliseconds(faker.date().past(365, TimeUnit.DAYS).time)
        )
    }
    .sortedByDescending { it.created }



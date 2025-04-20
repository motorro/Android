package com.motorro.repository.server

import com.motorro.repository.data.Book
import kotlin.time.Instant
import kotlin.uuid.Uuid

const val SERVER_HOST = "0.0.0.0"
const val SERVER_PORT = 8080
const val DELAY = 2000L

val books = listOf(
    Book(
        id = Uuid.parse("12578d75-be28-4b40-92b8-65eb47742b59"),
        authors = listOf("Ivan Ivanov"),
        title = "Kotlin for Beginners",
        cover = "https://picsum.photos/id/1/200/300",
        summary = "Kotlin for Beginners is a comprehensive guide to learning Kotlin programming language. It covers the basics of Kotlin syntax, data types, control flow, functions, and object-oriented programming concepts.",
        datePublished = Instant.parse("2023-01-01T00:00:00Z")
    ),
    Book(
        id = Uuid.parse("0de61354-78f5-4c3c-acae-50617dfaa2ac"),
        authors = listOf("Maria Petrova"),
        title = "Coroutines in Kotlin",
        cover = "https://picsum.photos/id/2/200/300",
        summary = "Coroutines in Kotlin is a deep dive into the world of coroutines. It explains how to use coroutines for asynchronous programming, how to manage concurrency, and how to build responsive applications.",
        datePublished = Instant.parse("2023-02-02T00:00:00Z")
    ),
    Book(
        id = Uuid.parse("434f31b2-463a-4e6e-b2d4-ed087f19fbda"),
        authors = listOf("John Doe"),
        title = "Data Science with Kotlin",
        cover = "https://picsum.photos/id/3/200/300",
        summary = "Data Science with Kotlin is a practical guide to using Kotlin for data science. It covers data manipulation, visualization, and machine learning using popular libraries like KotlinDL and Ktor.",
        datePublished = Instant.parse("2023-03-03T00:00:00Z")
    ),
    Book(
        id = Uuid.parse("dd552e11-b6f4-4e12-ab84-a5ce3b5fe784"),
        authors = listOf("Jane Smith"),
        title = "Kotlin for Android Development",
        cover = "https://picsum.photos/id/4/200/300",
        summary = "Kotlin for Android Development is a comprehensive guide to building Android applications using Kotlin. It covers Android architecture components, UI design, and best practices for Android development.",
        datePublished = Instant.parse("2023-04-04T00:00:00Z")
    )
)
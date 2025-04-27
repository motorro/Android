package com.motorro.repository.data

/**
 * Data class representing a new book.
 */
data class NewBook(
    val title: String,
    val authors: List<String>,
    val summary: String,
    val cover: String
)
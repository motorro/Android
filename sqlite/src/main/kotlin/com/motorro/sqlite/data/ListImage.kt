package com.motorro.sqlite.data

import android.net.Uri
import kotlinx.datetime.LocalDateTime

/**
 * List image data
 * @param path Image path
 * @param name Image name
 * @param dateTimeTaken Date and time image was taken
 */
data class ListImage(
    val path: Uri,
    val name: String,
    val dateTimeTaken: LocalDateTime
)
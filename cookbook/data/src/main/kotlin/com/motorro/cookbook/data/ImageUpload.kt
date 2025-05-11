package com.motorro.cookbook.data

import kotlinx.serialization.Serializable

/**
 * Image upload result
 */
@Serializable
data class ImageUpload(val image: Image)
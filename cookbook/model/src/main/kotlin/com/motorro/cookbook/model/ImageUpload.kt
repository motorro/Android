package com.motorro.cookbook.model

import kotlinx.serialization.Serializable

/**
 * Image upload result
 */
@Serializable
data class ImageUpload(val image: Image)
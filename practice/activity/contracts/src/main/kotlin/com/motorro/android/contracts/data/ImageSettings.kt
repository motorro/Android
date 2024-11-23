package com.motorro.android.contracts.data

/**
 * Image settings
 */
data class ImageSettings(val maxSize: Int, val quality: Int) {
    companion object {
        /**
         * Default image settings
         */
        val DEFAULT = ImageSettings(
            maxSize = 1024,
            quality = 80
        )
    }
}
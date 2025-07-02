package com.motorro.cookbook.model

import kotlinx.serialization.Serializable
import java.net.URI
import java.net.URL

/**
 * Image data
 */
@JvmInline
@Serializable
value class Image(val url: String) {
    override fun toString(): String  = url
    companion object {
        fun create(url: String) = Image(url)
        fun create(url: URL) = Image(url.toString())
        fun create(uri: URI) = Image(uri.toString())
    }
}
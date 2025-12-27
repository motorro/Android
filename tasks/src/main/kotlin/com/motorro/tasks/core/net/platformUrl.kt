package com.motorro.tasks.core.net

import com.motorro.tasks.core.getStaticConfig
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.URLBuilder
import io.ktor.http.appendPathSegments

/**
 * Builds server URL
 */
fun HttpRequestBuilder.platformUrl(endpoint: List<String>, extra: URLBuilder.() -> Unit = {}) {
    url {
        protocol  = getStaticConfig().protocol
        host = getStaticConfig().host
        port = getStaticConfig().port
        appendPathSegments(endpoint)
        extra()
    }
}
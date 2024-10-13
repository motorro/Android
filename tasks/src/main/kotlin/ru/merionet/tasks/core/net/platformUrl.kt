package ru.merionet.tasks.core.net

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.URLBuilder
import io.ktor.http.appendPathSegments
import ru.merionet.tasks.core.getStaticConfig

/**
 * Builds server URL using static application config
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
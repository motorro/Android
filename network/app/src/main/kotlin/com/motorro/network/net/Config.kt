package com.motorro.network.net

import okhttp3.HttpUrl

/**
 * Application config
 */
object Config {
    /**
     * Server protocol
     */
    const val scheme: String = "http"

    /**
     * Server host
     */
    const val host: String = "10.0.2.2"

    /**
     * Server port
     */
    const val port: Int = 8080

    /**
     * Base server URL
     */
    fun getBaseUrl(): HttpUrl = HttpUrl.Builder()
        .scheme(scheme)
        .host(host)
        .port(port)
        .build()
}



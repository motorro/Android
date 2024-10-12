package ru.merionet.tasks.core

import io.ktor.http.URLProtocol

/**
 * Application config
 */
object Config {
    /**
     * Server protocol
     */
    val protocol: URLProtocol = URLProtocol.HTTP

    /**
     * Server host
     */
    const val host: String = "10.0.2.2"

    /**
     * Server port
     */
    const val port: Int = 8080
}

/**
 * Retrieves application static config
 */
fun getStaticConfig(): Config = Config
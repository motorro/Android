package com.motorro.cookbook.server

import com.motorro.cookbook.data.Image
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.appendPathSegments
import org.jetbrains.exposed.v1.core.Transaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.nio.file.Path
import kotlin.io.path.pathString

inline fun <T> tQuery(crossinline block: Transaction.() -> T): T {
    return transaction() {
        block()
    }
}

fun Url.ofRelative(path: Path): Url = URLBuilder(this).appendPathSegments(path.map { it.pathString }).build()

fun Image.Companion.create(url: Url) = Image(url.toString())
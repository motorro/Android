package com.motorro.repository.net

import com.motorro.repository.data.Book
import com.motorro.repository.data.ListBook
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import kotlin.uuid.Uuid

/**
 * Ktor book service
 */
class KtorBooksApi(private val httpClient: HttpClient) : BooksApi {
    /**
     * Returns a list of books
     */
    override suspend fun getBookList(): Result<List<ListBook>> = coRunCatching {
        httpClient.get(Config.getBaseUrl().toUrl()) {
            url {
                appendPathSegments("books")
            }
        }.body()
    }

    /**
     * Returns a full book data
     */
    override suspend fun getBook(bookId: Uuid): Result<Book> = coRunCatching {
        httpClient.get(Config.getBaseUrl().toUrl()) {
            url {
                appendPathSegments("books", bookId.toString())
            }
        }.body()
    }
}
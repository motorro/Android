package com.motorro.repository

import android.app.Application
import androidx.fragment.app.Fragment
import com.motorro.repository.net.BooksApi
import com.motorro.repository.net.KtorBooksApi
import com.motorro.repository.net.createAppHttpClient
import com.motorro.repository.net.ktorAppHttpClient
import com.motorro.repository.usecase.GetBook
import com.motorro.repository.usecase.GetBookList
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient

class App : Application() {

    val okHttp: OkHttpClient by lazy {
        createAppHttpClient()
    }

    val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        encodeDefaults = true
    }

    val ktorClient by lazy {
        ktorAppHttpClient(okHttp, json)
    }

    val booksApi: BooksApi by lazy {
        KtorBooksApi(ktorClient)
    }

    val getBookList: GetBookList by lazy {
        GetBookList.Impl(booksApi)
    }

    val getBook: GetBook by lazy {
        GetBook.Impl(booksApi)
    }
}

fun Fragment.app(): App {
    return requireActivity().application as App
}
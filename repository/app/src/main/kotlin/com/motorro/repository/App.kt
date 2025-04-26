package com.motorro.repository

import android.app.Application
import androidx.fragment.app.Fragment
import com.motorro.repository.db.BooksDb
import com.motorro.repository.db.dao.BooksDao
import com.motorro.repository.net.BooksApi
import com.motorro.repository.net.KtorBooksApi
import com.motorro.repository.net.createAppHttpClient
import com.motorro.repository.net.ktorAppHttpClient
import com.motorro.repository.repository.BookListRepository
import com.motorro.repository.repository.BookRepository
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

    val booksDatabase: BooksDb by lazy {
        BooksDb.create(this)
    }

    val booksDao: BooksDao get() = booksDatabase.getBooksDao()

    val bookListRepository: BookListRepository by lazy {
        BookListRepository.Impl(booksDao, booksApi)
    }

    val bookRepository: BookRepository by lazy {
        BookRepository.Impl(booksDao, booksApi)
    }

    val getBookList: GetBookList by lazy {
        GetBookList.Impl(bookListRepository)
    }

    val getBook: GetBook by lazy {
        GetBook.Impl(bookRepository)
    }
}

fun Fragment.app(): App {
    return requireActivity().application as App
}
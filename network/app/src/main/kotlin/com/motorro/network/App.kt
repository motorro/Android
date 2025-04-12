package com.motorro.network

import android.app.Application
import androidx.fragment.app.Fragment
import com.motorro.network.net.createAppHttpClient
import com.motorro.network.net.usecase.GetUserList
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

    val getUserList: GetUserList by lazy {
        GetUserList.Impl(okHttp, json)
    }
}

fun Fragment.app(): App {
    return requireActivity().application as App
}
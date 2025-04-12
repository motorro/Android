package com.motorro.network

import android.app.Application
import androidx.fragment.app.Fragment
import com.motorro.network.net.UserApi
import com.motorro.network.net.createAppHttpClient
import com.motorro.network.net.createAppRetrofit
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

    val retrofit by lazy {
        createAppRetrofit(okHttp, json)
    }

    val userApi: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }

    val getUserList: GetUserList by lazy {
        GetUserList.Impl(userApi)
    }
}

fun Fragment.app(): App {
    return requireActivity().application as App
}
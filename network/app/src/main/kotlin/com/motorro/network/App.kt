package com.motorro.network

import android.app.Application
import androidx.fragment.app.Fragment
import com.motorro.network.net.KtorUserApi
import com.motorro.network.net.UserApi
import com.motorro.network.net.createAppHttpClient
import com.motorro.network.net.createAppRetrofit
import com.motorro.network.net.ktorAppHttpClient
import com.motorro.network.net.usecase.CreateUser
import com.motorro.network.net.usecase.DeleteUser
import com.motorro.network.net.usecase.GetProfile
import com.motorro.network.net.usecase.GetUserList
import com.motorro.network.session.SessionManager
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient

class App : Application() {
    val sessionManager: SessionManager by lazy {
        SessionManager.Impl()
    }

    val okHttp: OkHttpClient by lazy {
        createAppHttpClient(sessionManager)
    }

    val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        encodeDefaults = true
    }

    val retrofit by lazy {
        createAppRetrofit(okHttp, json)
    }

    val ktorClient by lazy {
        ktorAppHttpClient(sessionManager, json)
    }

    val userApi: UserApi by lazy {
        // Retrofit
        // retrofit.create(UserApi::class.java)

        // Ktor
        KtorUserApi(ktorClient)
    }

    val getUserList: GetUserList by lazy {
        GetUserList.Impl(userApi, sessionManager)
    }

    val getProfile: GetProfile by lazy {
        GetProfile.Impl(userApi)
    }

    val createUser: CreateUser by lazy {
        CreateUser.Impl(userApi)
    }

    val deleteUser: DeleteUser by lazy {
        DeleteUser.Impl(userApi)
    }
}

fun Fragment.app(): App {
    return requireActivity().application as App
}
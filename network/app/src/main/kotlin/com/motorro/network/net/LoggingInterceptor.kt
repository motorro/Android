package com.motorro.network.net

import android.util.Log
import okhttp3.Interceptor

class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        val response = chain.proceed(request)
        Log.i(TAG,"Request: ${request.method} ${request.url}")
        if (response.isSuccessful) {
            Log.i(TAG,"Response: ${response.code} ${response.message}")
        } else {
            Log.e(TAG,"Error: ${response.code} ${response.message}")
        }
        return response
    }

    companion object {
        const val TAG = "Network"
    }
}
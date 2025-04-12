package com.motorro.network.net

import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * A [CallAdapter.Factory] that converts a [Call] to a [Result].
 */
class ResultAdapterFactory : CallAdapter.Factory() {
    @Suppress("UNCHECKED_CAST")
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java || returnType !is ParameterizedType) {
            return null
        }

        val upperBound = getParameterUpperBound(0, returnType)

        return when {
            upperBound is ParameterizedType && upperBound.rawType == Result::class.java -> object : CallAdapter<Any, Call<Result<*>>> {
                override fun responseType(): Type = getParameterUpperBound(0, upperBound)
                override fun adapt(call: Call<Any>): Call<Result<*>> = ResultCall(call) as Call<Result<*>>
            }
            else -> null
        }
    }
}

private class ResultCall<T>(private val delegate: Call<T>): Call<Result<T>> {

    override fun enqueue(callback: Callback<Result<T>>) = delegate.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            callback.onResponse(this@ResultCall, processResponse(response))
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            callback.onResponse(this@ResultCall, Response.success(
                Result.failure(t)
            ))
        }
    })

    override fun execute(): Response<Result<T>> {
        val response = try {
            delegate.execute()
        } catch (e: Exception) {
            return Response.error(
                500,
                "Error executing call: ${e.message}".toResponseBody(null)
            )
        }

        return processResponse(response)
    }

    private fun processResponse(response: Response<T>): Response<Result<T>> {
        if (response.isSuccessful.not()) {
            return Response.success(Result.failure(IOException(
                "Error: ${response.code()}, Body: ${response.errorBody()?.string()}"
            )))
        }

        val body = response.body() ?: return Response.success(Result.failure(IOException(
            "Response body is null"
        )))

        return Response.success(Result.success(body))
    }

    override fun isExecuted(): Boolean = delegate.isExecuted
    override fun cancel() = delegate.cancel()
    override fun clone(): Call<Result<T>> = ResultCall(delegate.clone())
    override fun isCanceled(): Boolean = delegate.isCanceled
    override fun request(): Request = delegate.request()
    override fun timeout(): Timeout = delegate.timeout()
}
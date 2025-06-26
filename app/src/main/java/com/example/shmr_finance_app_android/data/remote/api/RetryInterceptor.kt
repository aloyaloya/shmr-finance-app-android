package com.example.shmr_finance_app_android.data.remote.api

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

const val SERVER_ERROR = 500

class RetryInterceptor(
    private val maxRetries: Int = 3,
    private val retryDelayMillis: Long = 2000L,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)

        var tryCount = 0
        while (!response.isSuccessful && response.code() == SERVER_ERROR && tryCount < maxRetries) {
            tryCount++
            response.close()

            runBlocking {
                delay(retryDelayMillis)
            }

            response = chain.proceed(request)
        }

        return response
    }
}
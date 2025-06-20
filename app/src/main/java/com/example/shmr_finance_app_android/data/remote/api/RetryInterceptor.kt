package com.example.shmr_finance_app_android.data.remote.api

import okhttp3.Interceptor
import okhttp3.Response

class RetryInterceptor(
    private val maxRetries: Int = 3,
    private val retryDelayMillis: Long = 1000L
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var response = chain.proceed(request)

        var tryCount = 0
        while (!response.isSuccessful && response.code() == 500 && tryCount < maxRetries) {
            tryCount++
            Thread.sleep(retryDelayMillis)
            response.close() // закрываем предыдущий response
            response = chain.proceed(request)
        }

        return response
    }
}
package com.example.shmr_finance_app_android.core.di

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Named

const val SERVER_ERROR = 500  // Код HTTP 500 (Internal Server Error)

/**
 * Интерсептор для повторных запросов при ошибках сервера.
 *
 * @param maxRetries - максимальное число попыток (по умолчанию 3).
 * @param retryDelayMillis - задержка между попытками в мс (по умолчанию 2000).
 */
class RetryInterceptor @Inject constructor(
    @Named("maxRetries") private val maxRetries: Int,
    @Named("retryDelay") private val retryDelayMillis: Long
) : Interceptor {

    /**
     * Перехватывает запрос и повторяет его при ошибках сервера.
     * @throws [IOException] если все попытки исчерпаны или возникла сетевая ошибка.
     */
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
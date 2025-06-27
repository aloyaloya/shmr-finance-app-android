package com.example.shmr_finance_app_android.data.remote.api

import retrofit2.HttpException
import java.io.IOException

/**
 * Обертка для безопасного выполнения сетевых запросов с обработкой ошибок.
 * Преобразует исключения в соответствующие [AppError] и возвращает [Result].
 *
 * @param call suspend-функция с API-вызовом.
 * @return [Result.success] с данными, если запрос успешен,
 * [Result.failure] с [AppError], если произошла ошибка.
 */
suspend fun <T> safeApiCall(call: suspend () -> T): Result<T> {
    return try {
        Result.success(call())
    } catch (e: IOException) {
        Result.failure(AppError.Network)
    } catch (e: HttpException) {
        Result.failure(AppError.ApiError())
    } catch (e: Exception) {
        Result.failure(AppError.Unknown())
    }
}
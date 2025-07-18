package com.example.shmr_finance_app_android.data.repository.utils

import com.example.shmr_finance_app_android.core.network.AppError
import com.example.shmr_finance_app_android.core.network.NetworkChecker
import com.example.shmr_finance_app_android.data.remote.api.safeApiCall

suspend inline fun <T, R> NetworkChecker.fetchWithCache(
    crossinline fetchLocal: suspend () -> T?,
    crossinline fetchRemote: suspend () -> T,
    crossinline saveRemote: suspend (T) -> Unit,
    map: (T) -> R
): Result<R> {
    val cached = try {
        fetchLocal()
    } catch (_: Exception) {
        null
    }

    if (!isNetworkAvailable()) {
        return cached?.let { Result.success(map(it)) } ?: Result.failure(AppError.Network)
    }

    return safeApiCall(
        call = {
            val remoteData = fetchRemote()
            saveRemote(remoteData)
            remoteData
        }
    ).fold(
        onSuccess = { Result.success(map(it)) },
        onFailure = { cached?.let { Result.success(map(it)) } ?: Result.failure(it) }
    )
}
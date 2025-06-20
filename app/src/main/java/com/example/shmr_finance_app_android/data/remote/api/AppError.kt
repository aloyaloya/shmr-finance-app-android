package com.example.shmr_finance_app_android.data.remote.api

sealed class AppError : Throwable() {

    data class ApiError(
        val code: Int,
        override val message: String?
    ) : AppError()

    object Network : AppError()

    data class Unknown(
        val original: Throwable
    ) : AppError()
}
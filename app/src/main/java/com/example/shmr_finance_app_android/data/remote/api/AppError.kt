package com.example.shmr_finance_app_android.data.remote.api

import androidx.annotation.StringRes
import com.example.shmr_finance_app_android.R

sealed class AppError(@StringRes val messageResId: Int) : Throwable() {
    class ApiError : AppError(R.string.api_error)
    object Network : AppError(R.string.network_error)
    class Unknown : AppError(R.string.unknown_error)
}
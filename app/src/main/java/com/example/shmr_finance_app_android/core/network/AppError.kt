package com.example.shmr_finance_app_android.core.network

import androidx.annotation.StringRes
import com.example.shmr_finance_app_android.R

sealed class AppError(@StringRes val messageResId: Int) : Throwable() {
    class ApiError : AppError(R.string.api_error)
    data object Network : AppError(R.string.network_error)
    class Unknown : AppError(R.string.unknown_error)
}
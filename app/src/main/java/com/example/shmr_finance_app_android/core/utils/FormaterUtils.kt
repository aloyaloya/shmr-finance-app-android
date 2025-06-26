package com.example.shmr_finance_app_android.core.utils

import java.text.DecimalFormat

fun String.formatWithSpaces(): String {
    return try {
        this.toBigDecimal().let { decimal ->
            DecimalFormat("#,###").format(decimal)
                .replace(",", " ")
        }
    } catch (e: Exception) {
        this
    }
}
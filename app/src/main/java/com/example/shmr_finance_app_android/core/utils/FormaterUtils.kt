package com.example.shmr_finance_app_android.core.utils

import java.text.DecimalFormat

/**
 * Отвечает за форматирование числовой строки с добавлением разделителей тысяч.
 * Преобразует строку в число и добавляет пробелы между тысячами.
 *
 * @return Отформатированная строка с пробелами в качестве разделителей тысяч
 * (например, "1000000" -> "1 000 000") или исходная строка при ошибке.
 */
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
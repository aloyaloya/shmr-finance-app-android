package com.example.shmr_finance_app_android.core.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private const val INPUT_DATE_FORMAT = "yyyy-MM-dd"
private const val OUTPUT_DATE_FORMAT = "d MMMM yyyy"

fun getCurrentDate(): String {
    val sdf = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.getDefault())
    return sdf.format(Date())
}

fun getStartAndEndOfCurrentMonth(): Pair<String, String> {
    val calendar = Calendar.getInstance()

    calendar.set(Calendar.DAY_OF_MONTH, 1)
    val startOfMonth = calendar.time

    calendar.add(Calendar.MONTH, 1)
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.add(Calendar.DATE, -1)
    val endOfMonth = calendar.time

    val sdf = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.getDefault())

    return Pair(
        sdf.format(startOfMonth),
        sdf.format(endOfMonth)
    )
}

fun formatDateToRussian(dateString: String): String {
    val inputFormat = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.getDefault())
    val outputFormat = SimpleDateFormat(OUTPUT_DATE_FORMAT, Locale("ru"))
    val date = inputFormat.parse(dateString) ?: return dateString
    return outputFormat.format(date)
}
package com.example.shmr_finance_app_android.core.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("yyyy-dd-MM", Locale.getDefault())
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

    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    return Pair(
        sdf.format(startOfMonth),
        sdf.format(endOfMonth)
    )
}

fun formatDateToRussian(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru"))
    val date = inputFormat.parse(dateString) ?: return dateString
    return outputFormat.format(date)
}
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

fun getStartOfCurrentMonth(): String {
    val calendar = Calendar.getInstance()

    calendar.set(Calendar.DAY_OF_MONTH, 1)

    val sdf = SimpleDateFormat(OUTPUT_DATE_FORMAT, Locale("ru"))

    return sdf.format(calendar.time)
}

fun getEndOfCurrentMonth(): String {
    val calendar = Calendar.getInstance()

    calendar.add(Calendar.MONTH, 1)
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.add(Calendar.DATE, -1)

    val sdf = SimpleDateFormat(OUTPUT_DATE_FORMAT, Locale("ru"))

    return sdf.format(calendar.time)
}

fun formatLongToHumanDate(timestamp: Long): String {
    val formatter = SimpleDateFormat(OUTPUT_DATE_FORMAT, Locale("ru"))
    return formatter.format(Date(timestamp))
}

fun formatHumanDateToIso(humanDate: String): String {
    return try {
        val humanFormatter = SimpleDateFormat(OUTPUT_DATE_FORMAT, Locale("ru"))
        val date = humanFormatter.parse(humanDate)
        val isoFormatter = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.US)
        date?.let { isoFormatter.format(it) } ?: ""
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

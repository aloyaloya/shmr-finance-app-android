package com.example.shmr_finance_app_android.core.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Отвечает за форматирование и преобразование дат в приложении.
 * Содержит константы форматов и функции для конвертации между ними.
 */

private const val INPUT_DATE_FORMAT = "yyyy-MM-dd" // Формат для запросов
private const val OUTPUT_DATE_FORMAT = "d MMMM yyyy" // Формат для отображения
private const val OUTPUT_DATE_TIME_FORMAT = "d MMMM HH:mm" // Формат для отображения даты и времени
private const val OUTPUT_TIME_FORMAT = "HH:mm" // Формат для отображения времени

/**
 * Отвечает за получение текущей даты в формате ISO.
 * @return Строка с текущей датой в ISO-формате [INPUT_DATE_FORMAT].
 */
fun getCurrentDate(): String {
    val sdf = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.getDefault())

    return sdf.format(Date())
}

/**
 * Отвечает за получение первого дня текущего месяца.
 * @return Строка с датой в формате [OUTPUT_DATE_FORMAT] (например, "1 июня 2025")
 */
fun getStartOfCurrentMonth(): String {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    val sdf = SimpleDateFormat(OUTPUT_DATE_FORMAT, Locale("ru"))

    return sdf.format(calendar.time)
}

/**
 * Отвечает за получение текущей даты.
 * @return Строка с текущей датой в формате [OUTPUT_DATE_FORMAT]
 */
fun getCurrentDateIso(): String {
    val calendar = Calendar.getInstance()
    val sdf = SimpleDateFormat(OUTPUT_DATE_FORMAT, Locale("ru"))

    return sdf.format(calendar.time)
}

/**
 * Отвечает за конвертацию timestamp в дату для отображения.
 * @param timestamp - время в миллисекундах
 * @return Отформатированная строка даты в формате [OUTPUT_DATE_FORMAT]
 * или пустая строка при ошибке
 */
fun formatLongToHumanDate(timestamp: Long): String {
    val formatter = SimpleDateFormat(OUTPUT_DATE_FORMAT, Locale("ru"))

    return formatter.format(Date(timestamp))
}

/**
 * Отвечает за конвертацию даты в ISO-формат.
 * @param humanDate - дата в формате [OUTPUT_DATE_FORMAT]
 * @return Дата в ISO-формате [INPUT_DATE_FORMAT]
 * или пустая строка при ошибке
 */
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

@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentTime(): String {
    return LocalTime.now().format(DateTimeFormatter.ofPattern(OUTPUT_TIME_FORMAT))
}

/**
 * Отвечает за объединение и форматирование даты и времени.
 * @param time - время для объединения
 * @param date - дата для объединения
 * @return Отформатированная строка в формате [OUTPUT_DATE_TIME_FORMAT] (например, "15 мая 14:30")
 */
@RequiresApi(Build.VERSION_CODES.O)
fun formatDateAndTime(time: LocalTime, date: LocalDate): String {
    val formatter = SimpleDateFormat(OUTPUT_DATE_TIME_FORMAT, Locale("ru"))
    val combinedDate = Date.from(date.atTime(time).atZone(ZoneId.systemDefault()).toInstant())

    return formatter.format(combinedDate)
}

@RequiresApi(Build.VERSION_CODES.O)
fun combineDateTimeToIso(dateStr: String, timeStr: String): String {
    val date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE)
    val time = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern(OUTPUT_TIME_FORMAT))

    return date.atTime(time)
        .atZone(ZoneId.systemDefault())
        .withZoneSameInstant(ZoneOffset.UTC)
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
}
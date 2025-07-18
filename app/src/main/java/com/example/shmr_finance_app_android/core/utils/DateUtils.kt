package com.example.shmr_finance_app_android.core.utils

import java.text.SimpleDateFormat
import java.time.Instant
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
private const val DEFAULT_OUTPUT_FORMAT = "dd.MM.yyyy HH:mm"
private const val OUTPUT_DATE_TIME_FORMAT = "d MMMM HH:mm" // Формат для отображения даты и времени
private const val OUTPUT_TIME_FORMAT = "HH:mm" // Формат для отображения времени
private const val OUTPUT_MONTH_YEAR = "LLLL yyyy" // Формат для отображения месяца и года

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
 * Конвертирует timestamp в строку с названием месяца и года
 * @param timestamp - время в миллисекундах
 * @return Строка в формате "Июль 2023" или пустая строка при ошибке
 */
fun formatLongToMonthYear(timestamp: Long): String {
    return try {
        val calendar = Calendar.getInstance().apply { timeInMillis = timestamp }
        val monthFormat = SimpleDateFormat(OUTPUT_MONTH_YEAR, Locale("ru"))
        monthFormat.format(calendar.time)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

/**
 * Возвращает текущий месяц и год в формате "Июль 2025"
 * @return Строка в формате "Месяц Год" (например, "Июль 2025")
 */
fun getCurrentMonthYear(): String {
    return try {
        val calendar = Calendar.getInstance()
        val monthFormat = SimpleDateFormat(OUTPUT_MONTH_YEAR, Locale("ru"))
        monthFormat.format(calendar.time)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

/**
 * Возвращает следующий месяц и год в формате "Июль 2025"
 * @return Строка в формате "Месяц Год" (например, "Август 2025")
 */
fun getNextMonthYear(): String {
    return try {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.MONTH, 1) // Переход на следующий месяц
        }
        val monthFormat = SimpleDateFormat(OUTPUT_MONTH_YEAR, Locale("ru"))
        monthFormat.format(calendar.time)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

/**
 * Конвертирует строку вида "Июль 2025" в ISO-дату первого дня месяца
 * @param monthYear - строка в формате "Июль 2025"
 * @return Дата в ISO-формате (первое число месяца) или пустая строка при ошибке
 */
fun getFirstDayOfMonthIso(monthYear: String): String {
    return try {
        val parser = SimpleDateFormat(OUTPUT_MONTH_YEAR, Locale.getDefault())
        val date = parser.parse(monthYear) ?: return ""

        val calendar = Calendar.getInstance().apply { time = date }
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        val isoFormatter = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.US)
        isoFormatter.format(calendar.time)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

/**
 * Конвертирует строку вида "Июль 2025" в ISO-дату последнего дня месяца
 * @param monthYear - строка в формате "Июль 2025"
 * @return Дата в ISO-формате (последнее число месяца) или пустая строка при ошибке
 */
fun getLastDayOfMonthIso(monthYear: String): String {
    return try {
        val parser = SimpleDateFormat(OUTPUT_MONTH_YEAR, Locale.getDefault())
        val date = parser.parse(monthYear) ?: return ""

        val calendar = Calendar.getInstance().apply { time = date }
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))

        val isoFormatter = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.US)
        isoFormatter.format(calendar.time)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
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

/**
 * Отвечает за конвертацию даты из ISO-формата в человекочитаемый формат.
 * @param isoDate - дата в формате [INPUT_DATE_FORMAT]
 * @return Дата в формате [OUTPUT_DATE_FORMAT] или пустая строка при ошибке
 */
fun formatIsoDateToHuman(isoDate: String): String {
    return try {
        val isoFormatter = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.US)
        val date = isoFormatter.parse(isoDate)
        val humanFormatter = SimpleDateFormat(OUTPUT_DATE_FORMAT, Locale("ru"))

        date?.let { humanFormatter.format(it) } ?: ""
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun getCurrentTime(): String {
    return LocalTime.now().format(DateTimeFormatter.ofPattern(OUTPUT_TIME_FORMAT))
}

/**
 * Отвечает за объединение и форматирование даты и времени.
 * @param time - время для объединения
 * @param date - дата для объединения
 * @return Отформатированная строка в формате [OUTPUT_DATE_TIME_FORMAT] (например, "15 мая 14:30")
 */
fun formatDateAndTime(time: LocalTime, date: LocalDate): String {
    val formatter = SimpleDateFormat(OUTPUT_DATE_TIME_FORMAT, Locale("ru"))
    val combinedDate = Date.from(date.atTime(time).atZone(ZoneId.systemDefault()).toInstant())

    return formatter.format(combinedDate)
}

fun combineDateTimeToIso(dateStr: String, timeStr: String): String {
    val date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE)
    val time = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern(OUTPUT_TIME_FORMAT))

    return date.atTime(time)
        .atZone(ZoneId.systemDefault())
        .withZoneSameInstant(ZoneOffset.systemDefault())
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
}

fun LocalDate.toHumanDate(): String =
    format(DateTimeFormatter.ofPattern(INPUT_DATE_FORMAT, Locale("ru")))

fun LocalTime.toHumanTime(): String =
    format(DateTimeFormatter.ofPattern(OUTPUT_TIME_FORMAT))

fun getCurrentIsoDateTime(): String {
    return DateTimeFormatter.ISO_INSTANT
        .withZone(ZoneOffset.systemDefault())
        .format(Instant.now())
}

/**
 * Форматирует timestamp в читаемую дату.
 *
 * @param timestamp Время в миллисекундах (если 0L - вернёт fallback)
 * @param pattern Желаемый формат даты (по умолчанию: "dd.MM.yyyy HH:mm")
 * @param fallback Строка, возвращаемая при ошибке (по умолчанию: "—")
 * @return Отформатированная строка или fallback при ошибке
 */
fun formatTimestamp(
    timestamp: Long,
    pattern: String = DEFAULT_OUTPUT_FORMAT,
    fallback: String = "-"
): String {
    if (timestamp == 0L) return fallback

    return try {
        SimpleDateFormat(pattern, Locale("ru")).format(Date(timestamp))
    } catch (e: Exception) {
        fallback
    }
}
package com.diffy.broke.utilcomponents

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import com.diffy.broke.dataclasses.SplittedDayRange
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun getTodayStartInMillis(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}

fun getStartOfWeekInMillis(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}

fun getStartOfMonthInMillis(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}

@Composable
fun dateInMillisToFormat(dateMillis: Long?): String {
    val currentDate = Calendar.getInstance()
    val selectedDate = Calendar.getInstance().apply {
        if (dateMillis != null) {
            timeInMillis = dateMillis
        }
    }

    return when {
        currentDate.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR) &&
                currentDate.get(Calendar.DAY_OF_YEAR) == selectedDate.get(Calendar.DAY_OF_YEAR) -> {
            "Today"
        }
        else -> {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dateFormat.format(selectedDate.time)
        }
    }
}

@SuppressLint("SimpleDateFormat")
fun formatDateFromMilliseconds(milliseconds: Long): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy - EEE")
    val date = Date(milliseconds)
    return dateFormat.format(date)
}

fun splitDateRange(startDateMillis: Long, endDateMillis: Long): List<SplittedDayRange> {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = startDateMillis

    val splittedDayRanges = mutableListOf<SplittedDayRange>()

    while (calendar.timeInMillis <= endDateMillis) {
        val startOfDay = calendar.clone() as Calendar
        startOfDay.set(Calendar.HOUR_OF_DAY, 0)
        startOfDay.set(Calendar.MINUTE, 0)
        startOfDay.set(Calendar.SECOND, 0)
        startOfDay.set(Calendar.MILLISECOND, 0)

        val endOfDay = calendar.clone() as Calendar
        endOfDay.set(Calendar.HOUR_OF_DAY, 23)
        endOfDay.set(Calendar.MINUTE, 59)
        endOfDay.set(Calendar.SECOND, 59)
        endOfDay.set(Calendar.MILLISECOND, 999)

        splittedDayRanges.add(SplittedDayRange(startOfDay.timeInMillis, endOfDay.timeInMillis))

        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }
    return splittedDayRanges
}
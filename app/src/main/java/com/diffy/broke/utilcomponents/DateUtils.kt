package com.diffy.broke.utilcomponents

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import com.diffy.broke.dataclasses.SplittedDayRange
import java.text.SimpleDateFormat
import java.time.Month
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

fun getTimeInMillisWithGivenDay(dayInMillis: Long): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = System.currentTimeMillis()
    calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR))
    calendar.set(Calendar.DAY_OF_YEAR, Calendar.getInstance().get(Calendar.DAY_OF_YEAR))
    calendar.timeInMillis = dayInMillis

    return calendar.timeInMillis
}

fun setSelectedMonthStartInMillis(selectedMonth: Int,selectedYear: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, selectedYear)
    calendar.set(Calendar.MONTH, selectedMonth)
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}

fun setSelectedMonthEndInMillis(selectedMonth: Int,selectedYear: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, selectedYear)
    calendar.set(Calendar.MONTH, selectedMonth)
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    return calendar.timeInMillis
}

@Composable
fun dateFormatter(dateMillis: Long?): String {
    val selectedDate = Calendar.getInstance().apply {
        if (dateMillis != null) {
            timeInMillis = dateMillis
        }
    }
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:SS", Locale.getDefault())
    return dateFormat.format(selectedDate.time)
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

fun monthAndDateFormatter(month: Int, year: Int): String {
    return Month.of(month+1).name + " - "+ year
}
@Composable
fun dateRangeFormatter(startDate: Long, endDate: Long): String {
    return dateInMillisToFormat(startDate)+ " - " + dateInMillisToFormat(endDate)
}
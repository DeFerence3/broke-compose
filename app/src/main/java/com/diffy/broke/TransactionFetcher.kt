package com.diffy.broke

import com.diffy.broke.database.Dao
import com.diffy.broke.dataclasses.DayRange
import com.diffy.broke.dataclasses.TransactionsInTimeperiod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.util.Calendar

fun getTransactions(mode: Int,dao: Dao): Flow<List<TransactionsInTimeperiod>> = flow {
    val minMaxDates = dao.getMinAndMaxDatetime().first()
    val transactionWithDate = mutableListOf<TransactionsInTimeperiod>()
    val splittedDates = splitDateRange(minMaxDates.minValue, minMaxDates.maxValue)

    when(mode) {
        1 -> {
            splittedDates.forEach { dates ->
                val transactions = dao.getTransactionsOrderByAscendOnDateRange(dates.startTimeMillis, dates.endTimeMillis)
                transactionWithDate.add(TransactionsInTimeperiod(dates.startTimeMillis, transactions.first()))
            }
        }
        2 -> {}
        3 -> {}
        4 -> {}
        5 -> {}
        6 -> {}
        7 -> {}
        8 -> {}
        9 -> {}
        10 -> {}
        11 -> {}
    }

    emit(transactionWithDate)
}


fun splitDateRange(startDateMillis: Long, endDateMillis: Long): List<DayRange> {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = startDateMillis

    val dayRanges = mutableListOf<DayRange>()

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

        dayRanges.add(DayRange(startOfDay.timeInMillis, endOfDay.timeInMillis))

        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }
    return dayRanges
}

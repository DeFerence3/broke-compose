package com.diffy.broke.dataclasses

import com.diffy.broke.database.Transactions

data class MinMaxDates(val minValue: Long, val maxValue: Long)
data class TransactionsInTimeperiod(val day: Long, val rangedTransactions: List<Transactions>)
data class DayRange(val startTimeMillis: Long, val endTimeMillis: Long)
package com.diffy.broke.dataclasses

import com.diffy.broke.database.Transactions
import com.diffy.broke.database.relations.TransactionWithTags

data class MinMaxDates(val minValue: Long,val maxValue: Long)
data class TransactionsInTimeperiod(val day: Long, val rangedTransactions: List<TransactionWithTags>)
data class SplittedDayRange(val startTimeMillis: Long, val endTimeMillis: Long)
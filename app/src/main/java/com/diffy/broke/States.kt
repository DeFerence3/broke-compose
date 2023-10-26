package com.diffy.broke

import com.diffy.broke.database.Tags
import com.diffy.broke.dataclasses.TransactionsInTimeperiod

data class States(
    val transactions: List<TransactionsInTimeperiod> = emptyList(),
    val tags: List<Tags> = emptyList(),
    val transactionName: String = "",
    val transactionAmount: String = "",
    val isExp: Boolean = true,
    val id: Int = 0,
    val isSelectingDateRange: Boolean = false,
    val isCreatingTransaction: Boolean = false,
    val isEditingTransaction: Boolean = false,
    val transactionDateInMillis: Long = 0,
    val transactionsOrderBy: OrderBy = OrderBy.ASENDING,
    val transactionsDateRange: DateRange = DateRange.ALLDAY,
    val startDateInMillis: Long = 1,
    val endDateInMillis: Long = 1
)

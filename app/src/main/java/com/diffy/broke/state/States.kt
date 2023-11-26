package com.diffy.broke.state

import com.diffy.broke.database.Tags
import com.diffy.broke.dataclasses.TransactionsInTimeperiod
import com.diffy.broke.state.DateRange
import com.diffy.broke.state.OrderBy
import com.diffy.broke.state.SortView

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
    val transactionsOrderBy: OrderBy = OrderBy.ASCENDING,
    val transactionsDateRange: DateRange = DateRange.MONTH,
    val sortView: SortView = SortView.ALL,
    val startDateInMillis: Long = 1,
    val endDateInMillis: Long = 1
)

package com.diffy.broke

import com.diffy.broke.database.Transactions

data class States(
    val transactions: List<Transactions> = emptyList(),
    val transactionName: String = "",
    val transactionAmount: String = "",
    val isExp: Boolean = true,
    val isSelectingDateRange: Boolean = false,
    val isCreatingTransaction: Boolean = false,
    val transactionDateInMillis: Long = 0,
    val transactionsOrderBy: OrderBy = OrderBy.ASENDING,
)

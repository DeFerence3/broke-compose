package com.diffy.broke

import com.diffy.broke.database.Expense
import com.diffy.broke.database.Group
import com.diffy.broke.database.Transactions

data class GroupState(
    val group: List<Group> = emptyList(),
    val transactions: List<Transactions> = emptyList(),
    val expense: List<Expense> = emptyList(),
    val packName: String = "",
    val totalExp: String = "",
    val isExp: Boolean = true,
    val isSelectingDateRange: Boolean = false,
    val isCreatingPack: Boolean = false,
    val isCreatingExpense: Boolean = false,
    val transactionsOrderBy: OrderBy = OrderBy.ASENDING,
)

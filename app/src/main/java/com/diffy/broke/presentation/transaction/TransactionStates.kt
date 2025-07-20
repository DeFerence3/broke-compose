@file:OptIn(ExperimentalTime::class)

package com.diffy.broke.presentation.transaction

import com.diffy.broke.domain.model.AccountHead
import com.diffy.broke.domain.model.Transaction
import kotlin.time.ExperimentalTime

data class TransactionStates(
    val transactions: List<Transaction> = emptyList(),
    val loadingState: Boolean = true,
    val selectedTransaction: com.diffy.broke.domain.model.Transaction? = null,
    val isSelectingDateRange: Boolean = false,
    val isAddEditDialogShowing: Boolean = false,
    val transactionDateInMillis: Long = 0,
    val transactionMsg: String = "",
    val accountHeads: List<AccountHead> = emptyList()
)
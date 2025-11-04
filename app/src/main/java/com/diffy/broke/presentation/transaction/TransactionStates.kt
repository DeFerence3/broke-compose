@file:OptIn(ExperimentalTime::class)

package com.diffy.broke.presentation.transaction

import com.diffy.broke.domain.model.Category
import com.diffy.broke.domain.model.Transaction
import com.diffy.broke.presentation.core.enums.ViewType
import kotlin.time.ExperimentalTime

data class TransactionStates(
    val transactions: List<Transaction> = emptyList(),
    val loadingState: Boolean = true,
    val selectedTransaction: Transaction? = null,
    val isSelectingDateRange: Boolean = false,
    val isAddEditDialogShowing: Boolean = false,
    val transactionDateInMillis: Long = 0,
    val transactionMsg: String = "",
    val categorys: List<Category> = emptyList(),
    var viewType: ViewType = ViewType.ThisMonth
)
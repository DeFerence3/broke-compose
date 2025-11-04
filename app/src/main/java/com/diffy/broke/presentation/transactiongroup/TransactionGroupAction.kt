package com.diffy.broke.presentation.transactiongroup

import com.diffy.broke.data.entity.TransactionGroup

sealed interface TransactionGroupAction {
    data object SaveTransactionHead : TransactionGroupAction
    data object AddTransactionGroup : TransactionGroupAction
    data object HideAddOrEditDialog : TransactionGroupAction
    data class SelectTransactionGroup(val accountGroup: TransactionGroup): TransactionGroupAction
}
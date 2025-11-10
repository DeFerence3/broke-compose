package com.diffy.broke.presentation.transactiongroup

sealed interface TransactionGroupAction {
    data object SaveTransactionHead : TransactionGroupAction
    data object AddTransactionGroup : TransactionGroupAction
}
package com.diffy.broke.presentation.transactiongroup

import com.diffy.broke.data.entity.TransactionGroup

data class TransactionGroupState(
    val isLoading: Boolean = false,
    val accountGroups: List<TransactionGroup> = emptyList(),
    val selectedAccountGroup: TransactionGroup? = null
)
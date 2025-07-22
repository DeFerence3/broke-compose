package com.diffy.broke.presentation.accountgroup

import com.diffy.broke.domain.model.AccountGroup

data class AccountGroupState(
    val isLoading: Boolean = false,
    val accountGroups: List<AccountGroup> = emptyList(),
    val selectedAccountGroup: AccountGroup? = null
)
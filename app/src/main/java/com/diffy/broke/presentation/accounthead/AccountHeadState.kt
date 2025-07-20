package com.diffy.broke.presentation.accounthead

import com.diffy.broke.domain.model.AccountGroup
import com.diffy.broke.domain.model.AccountHead

data class AccountHeadState(
    val isLoading: Boolean = false,
    val accountHeads: List<AccountHead> = emptyList(),
    val selectedAccountHead: AccountHead? = null,
    val isAddOrEditDialogShowing: Boolean = false,
    val groups: List<AccountGroup> = emptyList(),
    val searchQry: String = ""
)
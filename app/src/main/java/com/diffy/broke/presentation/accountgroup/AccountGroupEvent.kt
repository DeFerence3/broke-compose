package com.diffy.broke.presentation.accountgroup

import com.diffy.broke.domain.model.AccountGroup

sealed interface AccountGroupEvent {
    data object SaveAccountHead : AccountGroupEvent
    data object AddAccountGroup : AccountGroupEvent
    data object HideAddOrEditDialog : AccountGroupEvent
    data class SelectAccountGroup(val accountGroup: AccountGroup): AccountGroupEvent
}
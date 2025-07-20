package com.diffy.broke.presentation.accounthead

import com.diffy.broke.domain.model.AccountHead


sealed interface AccountHeadEvent {
    data object HideAddOrEditDialog: AccountHeadEvent
    data object AddAccountHead: AccountHeadEvent
    data object SaveAccountHead : AccountHeadEvent
    data class SelectAccountHead(val accountHead: AccountHead): AccountHeadEvent
    data class SearchAccountGroup(val query: String): AccountHeadEvent
}
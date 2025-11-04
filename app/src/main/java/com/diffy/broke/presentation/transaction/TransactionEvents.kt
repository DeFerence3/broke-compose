@file:OptIn(ExperimentalTime::class)

package com.diffy.broke.presentation.transaction

import com.diffy.broke.domain.model.Category
import com.diffy.broke.domain.model.Transaction
import com.diffy.broke.presentation.core.enums.ViewType
import kotlin.time.ExperimentalTime

sealed interface TransactionEvents {

    data object ShowAddEditDialog: TransactionEvents
    data object HideAddEditDialog: TransactionEvents

    data class SelectCategory(val category: Category): TransactionEvents
    data class SearchCategorys(val name:String): TransactionEvents
    data class SetTransactionName(val packName: String): TransactionEvents
    data class SetAmount(val transAmount: String): TransactionEvents
    data class SetSelectedTransaction(val transaction: Transaction?): TransactionEvents
    data class SetExpInc(val isExp: Boolean): TransactionEvents
    data class SetTransactionDate(val time: Long): TransactionEvents
    data class CreateTransaction(val transaction: Transaction): TransactionEvents
    data class DeleteTransaction(val transaction: Transaction): TransactionEvents
    data class SetId(val id: Int) : TransactionEvents

    data class SetViewType(val viewType: ViewType) : TransactionEvents

}
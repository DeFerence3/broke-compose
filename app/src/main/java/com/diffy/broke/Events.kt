package com.diffy.broke

import com.diffy.broke.database.Transactions

sealed interface Events {

    data object CreateTransaction: Events
    data object ShowAddDialog: Events
    data object HideAddDialog: Events
    data object ShowEditDialog: Events
    data object HideEditDialog: Events

    data class SetTransactionName(val packName: String): Events
    data class SetAmount(val transAmount: String): Events
    data class SetExpInc(val isExp: Boolean): Events
    data class SetTransactionDate(val time: Long): Events
    data class OrderPacks(val orderBy: OrderBy): Events
    data class DeleteTransaction(val transactions: Transactions): Events
    data class SortViewBy(val sortView: SortView): Events
    data class DateRangeBy(val dateRange: DateRange): Events
    data class SetStartDateInMillis(val startDateInMillis: Long): Events
    data class SetEndDateInMillis(val endDateInMillis: Long): Events
    data class SetId(val id: Int) : Events
}
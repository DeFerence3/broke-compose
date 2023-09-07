package com.diffy.broke

import com.diffy.broke.database.Transactions

sealed interface Events {

    object CreateTransaction: Events
    object ShowAddDialog: Events
    object HideAddDialog: Events
    object ShowEditDialog: Events
    object HideEditDialog: Events

    data class SetTransactionName(val packName: String): Events
    data class SetAmount(val transAmount: String): Events
    data class SetExpInc(val isExp: Boolean): Events
    data class SetTransactionDate(val time: Long): Events
    data class OrderPacks(val orderBy: OrderBy): Events
    data class DeleteTransaction(val transactions: Transactions): Events
    data class SortViewBy(val sortView: SortView): Events
    data class DateRangeBy(val dateRange: DateRange): Events
    data class SetId(val id: Int) : Events
}
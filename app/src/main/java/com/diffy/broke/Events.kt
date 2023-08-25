package com.diffy.broke

import com.diffy.broke.database.Transactions

sealed interface Events {

    object CreateGroup: Events
    object ShowDialog: Events
    object HideDialog: Events

    data class SetGroupName(val packName: String): Events
    data class SetAmount(val transAmount: String): Events
    data class SetExpInc(val isExp: Boolean): Events
    data class SetCurrentTime(val time: Long): Events
    data class OrderPacks(val orderBy: OrderBy): Events
    data class DeleteTransaction(val transactions: Transactions): Events
    data class SortViewBy(val sortView: SortView): Events
}
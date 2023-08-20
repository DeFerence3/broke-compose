package com.diffy.broke

import com.diffy.broke.database.Group
import com.diffy.broke.database.Transactions

sealed interface GroupEvent {

    object CreateGroup: GroupEvent
    object ShowDialog: GroupEvent
    object HideDialog: GroupEvent
    object HideDateRangePicker: GroupEvent
    object ShowDateRangePicker: GroupEvent

    data class SetGroupName(val packName: String): GroupEvent
    data class SetAmount(val transAmount: String): GroupEvent
    data class SetExpInc(val isExp: Boolean): GroupEvent
    data class SetCurrentTime(val time: String): GroupEvent
    data class OrderPacks(val orderBy: OrderBy): GroupEvent
    data class DeleteGroup(val group: Group): GroupEvent
    data class DeleteTransaction(val transactions: Transactions): GroupEvent
    data class SortViewBy(val sortView: SortView): GroupEvent
}
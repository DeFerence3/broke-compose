package com.diffy.broke

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diffy.broke.database.GroupDao
import com.diffy.broke.database.Transactions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModel(private val dao: GroupDao): ViewModel() {

    private val _state = MutableStateFlow(GroupState())
    private val _orderBy = MutableStateFlow(OrderBy.ASENDING)
    private val _sortBy = MutableStateFlow(SortView.ALL)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _pack = _sortBy
        .flatMapLatest { sortType ->
            when(sortType) {
                SortView.ALL -> {
                    _orderBy.flatMapLatest { orderBy ->
                        when(orderBy) {
                            OrderBy.ASENDING -> dao.getTransactionsOrderByMost()
                            OrderBy.DECENDING -> dao.getTransactionsOrderByLeast()
                        }
                    }
                }
                SortView.EXPENSE -> {
                    _orderBy.flatMapLatest { orderBy ->
                        when(orderBy) {
                            OrderBy.ASENDING -> dao.getExpenseOrderByMost()
                            OrderBy.DECENDING -> dao.getExpenseOrderByLeast()
                        }
                    }

                }
                SortView.INCOME -> {
                    _orderBy.flatMapLatest { orderBy ->
                        when(orderBy) {
                            OrderBy.ASENDING -> dao.getIncomeOrderByMost()
                            OrderBy.DECENDING -> dao.getIncomeOrderByLeast()
                        }
                    }
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(_state, _orderBy, _pack, _sortBy) { state, orderBy, pack, sortBy ->
        state.copy(
            transactions = pack,
            transactionsOrderBy = orderBy
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GroupState())

    fun onEvent(event: GroupEvent){
        when (event) {
            is GroupEvent.DeleteGroup -> {
                viewModelScope.launch{
                    dao.deletePack(event.group)
                }
            }
            is GroupEvent.DeleteTransaction -> {
                viewModelScope.launch {
                    dao.deleteTransaction(event.transactions)
                }
            }
            is GroupEvent.CreateGroup -> {
                val packName = state.value.packName
                val totalExp = state.value.totalExp
                val isExp = state.value.isExp

                if ( packName.isBlank() ){
                    return
                }

                val group = Transactions(
                    transTitle = packName,
                    transAmnt = totalExp.toInt(),
                    isExp = isExp,
                    day = System.currentTimeMillis()
                )
                viewModelScope.launch {
                    dao.upsertTransaction(group)
                }
                _state.update { it.copy(
                    isCreatingPack = false,
                    packName = "",
                    totalExp = ""
                ) }
            }
            is GroupEvent.HideDialog -> {
                _state.update { it.copy(
                    isCreatingPack = false
                ) }
            }
            is GroupEvent.ShowDateRangePicker -> {
                _state.update { it.copy(
                    isSelectingDateRange = true
                ) }
            }
            is GroupEvent.HideDateRangePicker -> {
                _state.update { it.copy(
                    isSelectingDateRange = false
                ) }
            }
            is GroupEvent.SetGroupName -> {
                _state.update { it.copy(
                    packName = event.packName
                ) }
            }
            is GroupEvent.OrderPacks -> {
                _orderBy.value = event.orderBy
            }
            is GroupEvent.SetAmount -> {
                _state.update { it.copy(
                    totalExp = event.transAmount
                ) }
            }
            is GroupEvent.SetExpInc -> {
                _state.update { it.copy(
                    isExp = event.isExp
                ) }
            }
            is GroupEvent.ShowDialog -> {
                _state.update { it.copy(
                    isCreatingPack = true
                ) }
            }

            is GroupEvent.SetCurrentTime -> TODO()
            is GroupEvent.SortViewBy -> {
                _sortBy.value = event.sortView
            }
        }
    }
}
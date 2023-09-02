package com.diffy.broke

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diffy.broke.database.Dao
import com.diffy.broke.database.Transactions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModel(private val dao: Dao): ViewModel() {

    private val _state = MutableStateFlow(States())
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

    val state = combine(_state, _orderBy, _pack) { state, orderBy, pack ->
        state.copy(
            transactions = pack,
            transactionsOrderBy = orderBy
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), States())

    fun onEvent(event: Events){
        when (event) {
            is Events.DeleteTransaction -> {
                viewModelScope.launch {
                    dao.deleteTransaction(event.transactions)
                }
            }
            is Events.CreateTransaction -> {
                val packName = state.value.transactionName
                val totalExp = state.value.transactionAmount
                val isExp = state.value.isExp
                val transactionDateInMillis = state.value.transactionDateInMillis
                val id = state.value.id

                if ( packName.isBlank() ){
                    return
                }

                val transaction = Transactions(
                    transTitle = packName,
                    transAmnt = totalExp.toInt(),
                    isExp = isExp,
                    day = transactionDateInMillis,
                    id = id
                )
                viewModelScope.launch {
                    dao.upsertTransaction(transaction)
                }
                _state.update { it.copy(
                    isCreatingTransaction = false,
                    isEditingTransaction = false,
                    transactionName = "",
                    transactionAmount = ""
                ) }
            }
            is Events.HideAddDialog -> {
                _state.update { it.copy(
                    isCreatingTransaction = false
                ) }
            }
            is Events.SetTransactionName -> {
                _state.update { it.copy(
                    transactionName = event.packName
                ) }
            }
            is Events.OrderPacks -> {
                _orderBy.value = event.orderBy
            }
            is Events.SetAmount -> {
                _state.update { it.copy(
                    transactionAmount = event.transAmount
                ) }
            }
            is Events.SetExpInc -> {
                _state.update { it.copy(
                    isExp = event.isExp
                ) }
            }
            is Events.ShowAddDialog -> {
                _state.update { it.copy(
                    isCreatingTransaction = true
                ) }
            }

            is Events.SetTransactionDate -> {
                _state.update { it.copy(
                    transactionDateInMillis = event.time
                ) }
            }
            is Events.SortViewBy -> {
                _sortBy.value = event.sortView
            }

            Events.HideEditDialog -> {
                _state.update { it.copy(
                    isEditingTransaction = false
                ) }
            }
            Events.ShowEditDialog -> {
                _state.update { it.copy(
                    isEditingTransaction = true
                ) }
            }

            is Events.SetId -> {
                _state.update { it.copy(
                    id = event.id
                ) }
            }
        }
    }
}
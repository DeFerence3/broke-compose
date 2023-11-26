package com.diffy.broke

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diffy.broke.database.Dao
import com.diffy.broke.database.Tags
import com.diffy.broke.database.Transactions
import com.diffy.broke.database.relations.TransactionWithTagsCrossRef
import com.diffy.broke.dataclasses.SummaryData
import com.diffy.broke.dataclasses.TransactionByTag
import com.diffy.broke.state.DateRange
import com.diffy.broke.state.Events
import com.diffy.broke.state.OrderBy
import com.diffy.broke.state.SortView
import com.diffy.broke.state.States
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModel(private val dao: Dao): ViewModel() {

    private val _state = MutableStateFlow(States())
    private val _orderBy = MutableStateFlow(OrderBy.ASCENDING)
    private val _sortBy = MutableStateFlow(SortView.ALL)
    private val _dateRangeBy = MutableStateFlow(DateRange.MONTH)
    private var _startDateInMillis = MutableStateFlow(0L)
    private var _endDateInMillis = MutableStateFlow(0L)

    private val _pack = combine(
        _sortBy,
        _orderBy,
        _startDateInMillis,
        _endDateInMillis
    ) { sortView, orderBy, startDateInMillis, endDateInMillis ->
        getTransactions(sortView, orderBy.ordinal, startDateInMillis, endDateInMillis, dao)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(_state, _orderBy, _pack, _dateRangeBy) { state, _ , pack, _ ->
        state.copy(
            transactions = pack,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3000), States())

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
                val tags = state.value.tags

                if ( packName.isBlank() || totalExp.isBlank() || tags.isEmpty() ){
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
                    val tagsid = dao.upsertTags(tags)
                    val transid = dao.upsertTransaction(transaction)
                    val transTagIdList = tagsid.map { tagId ->
                        TransactionWithTagsCrossRef(transid.toInt(), tagId.toInt())
                    }
                    dao.upsertTransactionsWithTags(transTagIdList)
                }

                clearState()

                _state.update { it.copy(
                    isCreatingTransaction = false,
                    isEditingTransaction = false,
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
                _state.update { it.copy(
                    sortView = event.sortView
                ) }.also {
                    _sortBy.value = _state.value.sortView
                }
            }
            is Events.HideEditDialog -> {
                _state.update { it.copy(
                    isEditingTransaction = false
                ) }
            }
            is Events.ShowEditDialog -> {
                _state.update { it.copy(
                    isEditingTransaction = true
                ) }
            }
            is Events.SetId -> {
                _state.update { it.copy(
                    id = event.id
                ) }
            }
            is Events.DateRangeBy -> {
                _dateRangeBy.value = event.dateRange
            }
            is Events.SetEndDateInMillis -> {
                _endDateInMillis.value = event.endDateInMillis
            }
            is Events.SetStartDateInMillis -> {
                _startDateInMillis.value = event.startDateInMillis
            }
            is Events.SetTags -> {
                _state.update { it.copy(
                    tags = event.tags
                ) }
            }
        }
    }

    fun clearState() {
        _state.update { it.copy(
            transactionName = "",
            transactionAmount = "",
            transactionDateInMillis = System.currentTimeMillis(),
            tags = emptyList(),
            id = 0
        ) }
    }

    fun getTotalSpendThisMonth(): Flow<SummaryData> {
        return dao.getOverView(_startDateInMillis.value,_endDateInMillis.value)
    }

    fun getTagsByTag(text: String): Flow<List<Tags>> {
        return dao.getTagsByTags("$text%")
    }

    fun getExpenseByTag(): Flow<List<TransactionByTag>> {
        return dao.getExpenseByTag(_startDateInMillis.value,_endDateInMillis.value)
    }

    fun getIncomeByTag(): Flow<List<TransactionByTag>> {
        return dao.getIncomeByTag(_startDateInMillis.value,_endDateInMillis.value)
    }
}
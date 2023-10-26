package com.diffy.broke

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diffy.broke.database.Dao
import com.diffy.broke.database.Transactions
import com.diffy.broke.database.relations.TransactionWithTagsCrossRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModel(private val dao: Dao): ViewModel() {

    private val _state = MutableStateFlow(States())
    private val _orderBy = MutableStateFlow(OrderBy.ASENDING)
    private val _sortBy = MutableStateFlow(SortView.ALL)
    private val _dateRangeBy = MutableStateFlow(DateRange.ALLDAY)
    private var _startDateInMillis = MutableStateFlow(System.currentTimeMillis())
    private var _endDateInMillis = MutableStateFlow(System.currentTimeMillis())

    init {
        viewModelScope.launch {
            _dateRangeBy.collect { dateRangeBy ->
                if (dateRangeBy == DateRange.ALLDAY) {
                    val minMaxDates = withContext(Dispatchers.IO) {
                        dao.getMinMaxDate().first()
                    }
                    _startDateInMillis.value = minMaxDates.minValue
                    _endDateInMillis.value = minMaxDates.maxValue
                }
            }
        }
    }

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
                val tags = state.value.tags

                if ( packName.isBlank() || totalExp.isBlank() ){
                    return
                }

                val transaction = Transactions(
                    transTitle = packName,
                    transAmnt = totalExp.toInt(),
                    isExp = isExp,
                    day = transactionDateInMillis,
                    id = id
                )

                viewModelScope.launch(Dispatchers.IO) {
                    val tagsid = dao.upsertTags(tags)
                    val transid = dao.upsertTransaction(transaction)
                    val transTagIdList = tagsid.map { tagId ->
                        TransactionWithTagsCrossRef(transid.toInt(), tagId.toInt())
                    }
                    dao.upsertTransactionsWithTags(transTagIdList)
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
}
@file:OptIn(ExperimentalTime::class)

package com.diffy.broke.presentation.transaction

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diffy.broke.Session
import com.diffy.broke.core.BrokeResponse
import com.diffy.broke.domain.model.Transaction
import com.diffy.broke.domain.use_case.accounthead.SearchAccountHeadUsecase
import com.diffy.broke.domain.use_case.transactions.CreateTransactionUseCase
import com.diffy.broke.domain.use_case.transactions.DeleteTransactionUseCase
import com.diffy.broke.domain.use_case.transactions.GetTransactionsUseCase
import com.diffy.broke.presentation.core.enums.ViewType
import com.diffy.broke.presentation.core.ui.util.getEndOfMonthInMillis
import com.diffy.broke.presentation.core.ui.util.getEndOfWeekInMillis
import com.diffy.broke.presentation.core.ui.util.getStartOfMonthInMillis
import com.diffy.broke.presentation.core.ui.util.getStartOfWeekInMillis
import com.diffy.broke.presentation.core.ui.util.getTodayEndInMillis
import com.diffy.broke.presentation.core.ui.util.getTodayStartInMillis
import com.diffy.broke.presentation.transaction.TransactionEvents.HideAddEditDialog
import com.diffy.broke.presentation.transaction.TransactionEvents.SetSelectedTransaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@HiltViewModel
class TransactionVM @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val searchAccountHeadUsecase: SearchAccountHeadUsecase,
    private val session: Session
): ViewModel() {

    private val _state = MutableStateFlow(TransactionStates())
    val state: StateFlow<TransactionStates> = _state.asStateFlow()

    init {
       getTransactions()
    }

    private fun getTransactions() {
        viewModelScope.launch {
            val startTime = when(_state.value.viewType){
                is ViewType.Custom -> (_state.value.viewType as ViewType.Custom).start
                ViewType.ThisMonth -> getStartOfMonthInMillis()
                ViewType.ThisWeek -> getStartOfWeekInMillis()
                ViewType.Today -> getTodayStartInMillis()
            }

            val endTime = when(_state.value.viewType){
                is ViewType.Custom -> (_state.value.viewType as ViewType.Custom).end
                ViewType.ThisMonth -> getEndOfMonthInMillis()
                ViewType.ThisWeek -> getEndOfWeekInMillis()
                ViewType.Today -> getTodayEndInMillis()
            }
            getTransactionsUseCase.invoke(
                startTime = Instant.fromEpochMilliseconds(startTime),
                endTime = Instant.fromEpochMilliseconds(endTime),
                transactionsOrderBy = session.orderBy,
                sortView = session.sortView
            ).collectLatest { transactionResult ->
                when(transactionResult){
                    is BrokeResponse.Success -> {
                        updateState { it.copy(transactions = transactionResult.data, loadingState =  false) }
                    }
                    else -> {
                        Log.i(
                            "Broke",
                            "Loading or error something---> $transactionResult"
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: TransactionEvents){
        when (event) {
            is TransactionEvents.DeleteTransaction -> {
                viewModelScope.launch {
                    deleteTransactionUseCase(event.transaction)
                }
            }
            is TransactionEvents.CreateTransaction -> {
                val transactionWithTags = validateTransaction(event.transaction)
                if (transactionWithTags != null){
                    viewModelScope.launch {
                        createTransactionUseCase.invoke(transactionWithTags).collect{
                            Log.i("Broke", "CreateTrans---> $it")
                        }
                    }
                    onEvent(HideAddEditDialog)
                    onEvent(SetSelectedTransaction(null))
                } else {
                    return
                }
            }
            is HideAddEditDialog -> updateState { it.copy(isAddEditDialogShowing = false) }
            is TransactionEvents.SetTransactionName -> {
//                _state.update { it.copy(
//                    transactionName = event.packName
//                ) }
            }
            is TransactionEvents.SetAmount -> {
//                _state.update { it.copy(
//                    transactionAmount = event.transAmount
//                ) }
            }
            is TransactionEvents.SetExpInc -> {
//                _state.update { it.copy(
//                    isExp = event.isExp
//                ) }
            }
            is TransactionEvents.ShowAddEditDialog -> updateState { it.copy(isAddEditDialogShowing = true) }

            is TransactionEvents.SetTransactionDate -> {
                _state.update { it.copy(
                    transactionDateInMillis = event.time
                ) }
            }
            is TransactionEvents.SetId -> {
//                _state.update { it.copy(
//                    id = event.id
//                ) }
            }

            is SetSelectedTransaction -> updateState { it.copy(selectedTransaction = event.transaction) }
            is TransactionEvents.SearchAccountHeads -> {
                viewModelScope.launch {
                    searchAccountHeadUsecase.invoke(event.name).collectLatest {
                        updateState { transactionStates ->
                            transactionStates.copy(accountHeads = it)
                        }
                    }
                }
            }

            is TransactionEvents.SetViewType -> updateState { it.copy(viewType = event.viewType) }.also { getTransactions() }
        }
    }

    private fun validateTransaction(transaction: Transaction): Transaction? {
        return if ( transaction.notes.isBlank()) {
            null
        }else if ( transaction.amount.isNullOrBlank() || transaction.amount.toFloatOrNull() == null) {
            null
        }else{
            transaction
        }
    }

/*
    private fun setTransactions(
        start: Long,
        end: Long,
        transactionsOrderBy: OrderBy,
        sortView: SortView
    ) {
        viewModelScope.launch {
            getTransactionsUseCase.invoke(start,end,transactionsOrderBy,sortView).collectLatest{ response ->
                when(response) {
                    is BrokeResponse.Empty -> updateState { it.copy(
                        transactionMsg = response.message,
                        loadingState = false
                    ) }
                    is BrokeResponse.Error -> updateState { it.copy(
                        transactionMsg = response.message,
                        loadingState = false
                    ) }
                    is BrokeResponse.Loading -> updateState { it.copy(
                        transactionMsg = response.message,
                        loadingState = true
                    ) }
                    is BrokeResponse.NetworkError -> updateState { it.copy(
                        transactionMsg = response.message,
                        loadingState = false
                    ) }
                    is BrokeResponse.Success -> updateState { it.copy(
                        transactions = response.data,
                        loadingState = false
                    ) }
                }
            }
        }
    }
*/
    private inline fun updateState(update: (TransactionStates) -> TransactionStates) {
        _state.value = update(_state.value)
    }
}
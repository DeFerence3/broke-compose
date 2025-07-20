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
import com.diffy.broke.presentation.transaction.TransactionEvents.HideAddEditDialog
import com.diffy.broke.presentation.transaction.TransactionEvents.SetSelectedTransaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
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

    private val _transactionState = MutableStateFlow(TransactionStates())
    val state: StateFlow<TransactionStates> = _transactionState.asStateFlow()

    val tempState: StateFlow<TransactionStates> = flow {
        _transactionState.update { it.copy(loadingState = true) }
        emit(TransactionStates(loadingState = true))

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),_transactionState.value)

    init {
       getTransactions()
    }

    private fun getTransactions() {
        viewModelScope.launch {
            getTransactionsUseCase.invoke(
                startTime = Instant.fromEpochMilliseconds(session.startDate),
                endTime = Instant.fromEpochMilliseconds(session.endDate),
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
//                _transactionState.update { it.copy(
//                    transactionName = event.packName
//                ) }
            }
            is TransactionEvents.SetAmount -> {
//                _transactionState.update { it.copy(
//                    transactionAmount = event.transAmount
//                ) }
            }
            is TransactionEvents.SetExpInc -> {
//                _transactionState.update { it.copy(
//                    isExp = event.isExp
//                ) }
            }
            is TransactionEvents.ShowAddEditDialog -> updateState { it.copy(isAddEditDialogShowing = true) }

            is TransactionEvents.SetTransactionDate -> {
                _transactionState.update { it.copy(
                    transactionDateInMillis = event.time
                ) }
            }
            is TransactionEvents.SetId -> {
//                _transactionState.update { it.copy(
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
        }
    }

    private fun validateTransaction(transaction: Transaction): Transaction? {
        if ( transaction.notes.isNullOrBlank()) {
            return null
        }else if ( transaction.amount.isNullOrBlank() || transaction.amount.toFloatOrNull() == null) {
            return null
        }else{
            return transaction
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
        _transactionState.value = update(_transactionState.value)
    }
}
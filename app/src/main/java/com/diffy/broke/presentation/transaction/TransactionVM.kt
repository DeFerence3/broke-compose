@file:OptIn(ExperimentalTime::class)

package com.diffy.broke.presentation.transaction

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diffy.broke.Session
import com.diffy.broke.core.BrokeResponse
import com.diffy.broke.domain.model.Transaction
import com.diffy.broke.domain.use_case.category.SearchCategoryUsecase
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
    private val searchCategoryUsecase: SearchCategoryUsecase,
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
                val transaction = state.value.selectedTransaction
                if (transaction != null){
                    val validatedTransaction = validateTransaction(transaction)
                    if (validatedTransaction != null){
                        viewModelScope.launch {
                            createTransactionUseCase.invoke(validatedTransaction).collect{
                                Log.i("Broke", "CreateTrans---> $it")
                            }
                        }
                        onEvent(HideAddEditDialog)
                        onEvent(SetSelectedTransaction(null))
                    } else {
                        return
                    }
                }
            }
            is HideAddEditDialog -> updateState { it.copy(selectedTransaction = null) }
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
            is TransactionEvents.SearchCategorys -> {
                viewModelScope.launch {
                    searchCategoryUsecase.invoke(event.name).collectLatest {
                        updateState { transactionStates ->
                            transactionStates.copy(categorys = it)
                        }
                    }
                }
            }

            is TransactionEvents.SetViewType -> updateState { it.copy(viewType = event.viewType) }.also { getTransactions() }
            is TransactionEvents.SelectCategory -> updateState { it.copy(selectedTransaction = it.selectedTransaction?.copy(category = event.category)) }
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

    private inline fun updateState(update: (TransactionStates) -> TransactionStates) {
        _state.value = update(_state.value)
    }
}
package com.diffy.broke.presentation.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diffy.broke.domain.model.CategorySpend
import com.diffy.broke.domain.repository.CategoryRepo
import com.diffy.broke.domain.repository.TransactionsRepo
import com.diffy.broke.presentation.transactiongroup.TransactionGroupEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardVM @Inject constructor(
    private val transactionsRepo: TransactionsRepo,
    private val categoryRepo: CategoryRepo
): ViewModel() {
    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state
        .onStart {
            fetchInitialData()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),_state.value)

    private val _eventChannel = Channel<TransactionGroupEvent>(Channel.BUFFERED)
    val eventChannel = _eventChannel.receiveAsFlow()

    fun onAction(action: DashboardAction){
        when(action){
            DashboardAction.OnNextMonth -> {
                updateState { it.copy(currentMonth = it.currentMonth.next()) }
                fetchInitialData()
            }
            DashboardAction.OnPreviousMonth -> {
                updateState { it.copy(currentMonth = it.currentMonth.previous()) }
                fetchInitialData()
            }
        }
    }

    fun fetchInitialData(){
        viewModelScope.launch {
            val state = _state.value
            transactionsRepo.getOverView(
                month = state.currentMonth.position,
                year = state.currentYear
            ).collect { overView ->
                updateState { it.copy(overView = overView)}
            }
        }

        viewModelScope.launch {
            transactionsRepo.getTodaySpend().collectLatest { todaySpend ->
                updateState { it.copy(todaySpend = todaySpend)}
            }
        }

        viewModelScope.launch {
            val state = _state.value
            categoryRepo.getCategorySpend(
                month = state.currentMonth.position,
                year = state.currentYear
            ).collectLatest { categorySpend ->
                updateState { it.copy(categorySpendM = categorySpend.map { CategorySpend(
                    name = it.name,
                    spent = it.spent,
                    budget = it.budget,
                    icon = Icons.Default.Folder,
                    Color(0xFFE8F5E9), Color(0xFF43A047)
                ) })}
            }
        }
    }

    private inline fun updateState(update: (DashboardState) -> DashboardState) = _state.update { update(_state.value) }

}
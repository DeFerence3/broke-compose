package com.diffy.broke.presentation.transactiongroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diffy.broke.data.entity.TransactionGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionGroupVM @Inject constructor(

) : ViewModel() {

    private val _state = MutableStateFlow(TransactionGroupState())
    val state: StateFlow<TransactionGroupState> = _state.asStateFlow()

    private val oneTimeEventChannel = Channel<TransactionGroupEvent>(Channel.BUFFERED)
    val oneTimeEventChannelFlow = oneTimeEventChannel.receiveAsFlow()

    init {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            fetchInitialData()
        }
    }

    fun onEvent(event: TransactionGroupAction) {
        when(event){
            TransactionGroupAction.AddTransactionGroup -> {updateState { it.copy(selectedTransactionGroup = TransactionGroup(
                id = 0,
                name = "",
                description = null
            )) }}
            TransactionGroupAction.SaveTransactionHead -> TODO()
        }
    }

    private inline fun updateState(update: (TransactionGroupState) -> TransactionGroupState) = _state.update { update(_state.value) }

    private suspend fun fetchInitialData(){

    }
}
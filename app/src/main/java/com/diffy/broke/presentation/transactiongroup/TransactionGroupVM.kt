package com.diffy.broke.presentation.transactiongroup

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@dagger.hilt.android.lifecycle.HiltViewModel
class TransactionGroupVM @javax.inject.Inject constructor(

) : androidx.lifecycle.ViewModel() {

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

    }

    private inline fun updateState(update: (TransactionGroupState) -> TransactionGroupState) = _state.update { update(_state.value) }

    private suspend fun fetchInitialData(){

    }
}
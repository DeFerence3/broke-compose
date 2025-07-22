package com.diffy.broke.presentation.accountgroup

import androidx.lifecycle.viewModelScope
import com.diffy.broke.domain.model.AccountGroup
import com.diffy.broke.domain.use_case.accountgroup.SearchAccountGroupUsecase
import com.diffy.broke.domain.use_case.accountgroup.UpseartAccountGroupUsecase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@dagger.hilt.android.lifecycle.HiltViewModel
class AccountGroupVM @javax.inject.Inject constructor(
    private val upseartAccountGroupUsecase: UpseartAccountGroupUsecase,
    private val searchAccountGroupUsecase: SearchAccountGroupUsecase
) : androidx.lifecycle.ViewModel() {

    private val _state = MutableStateFlow(AccountGroupState())
    val state: StateFlow<AccountGroupState> = _state.asStateFlow()

    private val oneTimeEventChannel = Channel<AccountGroupOneTimeEvent>(Channel.BUFFERED)
    val oneTimeEventChannelFlow = oneTimeEventChannel.receiveAsFlow()

    init {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            fetchInitialData()
        }
    }

    fun onEvent(event: AccountGroupEvent) {
        when (event) {
            is AccountGroupEvent.SaveAccountHead -> {
                val currentSelectedAccountGroup = state.value.selectedAccountGroup
                if (!currentSelectedAccountGroup?.accountGroupName.isNullOrBlank()) {
                    viewModelScope.launch {
                        try {
                            upseartAccountGroupUsecase(currentSelectedAccountGroup).collectLatest {
                                fetchInitialData()
                                oneTimeEventChannel.send(AccountGroupOneTimeEvent.Success("Account Group Saved Successfully!"))
                                updateState{ it.copy(selectedAccountGroup = null) } // Hide dialog and clear selection
                            }
                        } catch (e: Exception) {
                            oneTimeEventChannel.send(AccountGroupOneTimeEvent.Error("Failed to save account group: ${e.localizedMessage}"))
                        }
                    }
                } else {
                    viewModelScope.launch {
                        oneTimeEventChannel.send(AccountGroupOneTimeEvent.Error("Account Group Name cannot be empty."))
                    }
                }
            }
            AccountGroupEvent.AddAccountGroup -> updateState { it.copy(selectedAccountGroup = AccountGroup.new()) }
            AccountGroupEvent.HideAddOrEditDialog -> updateState { it.copy(selectedAccountGroup = null) }
            is AccountGroupEvent.SelectAccountGroup -> updateState { it.copy(selectedAccountGroup = event.accountGroup) }
        }
    }

    private inline fun updateState(update: (AccountGroupState) -> AccountGroupState) = _state.update { update(_state.value) }

    private suspend fun fetchInitialData(){
        try {
            searchAccountGroupUsecase("").collectLatest { groups ->
                updateState{ it.copy(accountGroups = groups, isLoading = false) }
            }
        } catch (e: Exception) {
            oneTimeEventChannel.send(AccountGroupOneTimeEvent.Error("Failed to load account groups: ${e.localizedMessage}"))
            updateState{ it.copy(isLoading = false) }
        }
    }
}
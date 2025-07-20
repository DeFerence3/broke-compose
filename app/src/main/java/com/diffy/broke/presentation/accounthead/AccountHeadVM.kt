package com.diffy.broke.presentation.accounthead

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.diffy.broke.domain.use_case.accountgroup.SearchAccountGroupUsecase
import com.diffy.broke.domain.use_case.accounthead.SearchAccountHeadUsecase
import com.diffy.broke.domain.use_case.accounthead.UpseartAccountHeadUsecase
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@dagger.hilt.android.lifecycle.HiltViewModel
class AccountHeadVM @javax.inject.Inject constructor(
    private val upsertAccountHeadUsecase: UpseartAccountHeadUsecase,
    private val searchAccountHeadUsecase: SearchAccountHeadUsecase,
    private val searchAccountGroupUsecase: SearchAccountGroupUsecase
) : androidx.lifecycle.ViewModel() {
    private val _state = kotlinx.coroutines.flow.MutableStateFlow(AccountHeadState())
    val state: kotlinx.coroutines.flow.StateFlow<AccountHeadState> = _state.asStateFlow()

    private val _oneTimeEventChannelFlow = kotlinx.coroutines.channels.Channel<AccountHeadOneTimeEvent>()
    val oneTimeEventChannelFlow = _oneTimeEventChannelFlow.receiveAsFlow()

    init {
        viewModelScope.launch {
            searchAccountHeadUsecase("").collectLatest{ heads ->
                updateState { it.copy(accountHeads = heads) }
            }
        }
    }

    fun onEvent(event: AccountHeadEvent) {
        when (event) {
            AccountHeadEvent.AddAccountHead -> updateState { it.copy(isAddOrEditDialogShowing = true) }
            AccountHeadEvent.HideAddOrEditDialog -> updateState { it.copy(isAddOrEditDialogShowing = false, selectedAccountHead = null) }
            is AccountHeadEvent.SelectAccountHead -> updateState { it.copy(selectedAccountHead = event.accountHead) }
            AccountHeadEvent.SaveAccountHead -> {
                val currentSelectedAccountGroup = state.value.selectedAccountHead
                if (!currentSelectedAccountGroup?.accountHeadName.isNullOrBlank() && currentSelectedAccountGroup.accountGroup != null)
                viewModelScope.launch {
                    upsertAccountHeadUsecase(accountHead = currentSelectedAccountGroup).collectLatest {
                        Log.i("Broke", "UpseartAccountHead---> $it")
                    }
                }
            }

            is AccountHeadEvent.SearchAccountGroup -> {
                viewModelScope.launch {
                    searchAccountGroupUsecase(event.query).collectLatest { groups ->
                        Log.i("Broke", "Groups---> $groups")
                        updateState { it.copy(groups = groups) }
                    }
                }
            }
        }
    }

    private inline fun updateState(update: (AccountHeadState) -> AccountHeadState) {
        _state.value = update(_state.value)
    }
}
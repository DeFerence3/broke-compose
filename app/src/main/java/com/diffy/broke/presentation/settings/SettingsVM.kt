package com.diffy.broke.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diffy.broke.core.AppPreferences
import com.diffy.broke.core.Result
import com.diffy.broke.domain.repository.CommonDbRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsVM @Inject constructor (
	private val preferences: AppPreferences,
	private val commonDbRepo: CommonDbRepo
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
	val state: StateFlow<SettingsState> =  _state
		.onStart {
			fetchInitialData()
		}
		.stateIn(
			viewModelScope,
			SharingStarted.WhileSubscribed(5000L),
			SettingsState()
		)

	private val _eventChanel = Channel<SettingsOneTimeEvents>()
	val eventChanel = _eventChanel.receiveAsFlow()

	fun onEvent(event: SettingsEvents) {
		when(event) {
			SettingsEvents.OnBackup -> {
				viewModelScope.launch {
					commonDbRepo.backup().collectLatest {result ->
						when(result){
							is Result.failure -> showToast(result.e.message ?: "Unknown error")
							is Result.success -> showToast(result.data)
						}
					}
				}
			}
			SettingsEvents.OnRestore -> {
				viewModelScope.launch {
					commonDbRepo.restore().collectLatest{ result ->
						when(result){
							is Result.failure -> showToast(result.e.message ?: "Unknown error")
                            is Result.success -> showToast(result.data)
                        }
					}
				}
			}
		}
	}

	private fun fetchInitialData() {
		viewModelScope.launch{
			preferences.lastBackupDate.collectLatest { date ->
				updateState { it.copy(lastBackupDate = date) }
			}
		}
		viewModelScope.launch{
			preferences.lastRestoreDate.collectLatest { date ->
				updateState { it.copy(lastRestoreDate = date) }
			}
		}
	}

	private inline fun updateState(update: (SettingsState) -> SettingsState) {
		_state.value = update(_state.value)
	}

	private fun showToast(message: String){
		viewModelScope.launch {
			_eventChanel.send(SettingsOneTimeEvents.ShowToast(message))
		}
	}
}
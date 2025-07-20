package com.diffy.broke.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diffy.broke.core.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsVM @Inject constructor (
	private val preferences: AppPreferences
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

	fun onEvent(event: SettingsEvents) {
		when(event) {
			SettingsEvents.OnBackup -> {

			}
			SettingsEvents.OnRestore -> {

			}
		}
	}

	private fun fetchInitialData() {
		viewModelScope.launch{
			preferences.lastBackupDate.collectLatest { date ->
				updateState { it.copy(lastBackupDate = date) }
			}

			preferences.lastRestoreDate.collectLatest { date ->
				updateState { it.copy(lastRestoreDate = date) }
			}
		}
	}

	private inline fun updateState(update: (SettingsState) -> SettingsState) {
		_state.value = update(_state.value)
	}
}
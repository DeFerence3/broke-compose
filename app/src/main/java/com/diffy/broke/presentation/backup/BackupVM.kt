package com.diffy.broke.presentation.backup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class BackupVM @Inject constructor (

) : ViewModel() {
    private val _backupState = MutableStateFlow(String())
	val state: StateFlow<String> = _backupState.asStateFlow()



	private inline fun updateState(update: (String) -> String) {
		_backupState.value = update(_backupState.value)
	}
}
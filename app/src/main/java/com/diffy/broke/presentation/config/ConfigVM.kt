package com.diffy.broke.presentation.config

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ConfigVM @Inject constructor(

) : ViewModel() {
    private val _state = MutableStateFlow(ConfigState())
    val state: StateFlow<ConfigState> = _state.asStateFlow()

    fun onEvent(event: ConfigEvent) {
        when (event) {
            ConfigEvent.OnSubmit -> TODO()
        }
    }

    private inline fun updateState(update: (ConfigState) -> ConfigState) {
        _state.update { update(_state.value) }
    }
}
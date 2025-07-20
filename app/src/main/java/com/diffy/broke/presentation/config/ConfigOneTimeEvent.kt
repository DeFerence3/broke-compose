package com.diffy.broke.presentation.config

sealed interface ConfigOneTimeEvent{
    data class ShowToast(val message: String): ConfigOneTimeEvent
}
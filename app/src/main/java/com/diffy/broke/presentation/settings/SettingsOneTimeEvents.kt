package com.diffy.broke.presentation.settings

sealed interface SettingsOneTimeEvents {
    data class ShowToast(val message: String): SettingsOneTimeEvents
}
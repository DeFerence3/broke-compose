package com.diffy.broke.presentation.settings

sealed interface SettingsEvents {
    data object OnBackup: SettingsEvents
    data object OnRestore: SettingsEvents
}
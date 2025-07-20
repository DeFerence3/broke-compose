package com.diffy.broke.presentation.settings

import java.util.Date

data class SettingsState(
    val lastBackupDate: Date? = null,
    val lastRestoreDate: Date? = null
)
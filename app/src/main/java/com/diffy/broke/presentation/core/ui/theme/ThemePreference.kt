package com.diffy.broke.presentation.core.ui.theme

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.diffy.broke.R
import com.diffy.broke.presentation.core.ui.theme.conf.DarkTheme
import com.diffy.broke.presentation.core.ui.theme.conf.ThemeType

data class ThemePreference(
    val darkMode: DarkTheme = DarkTheme.System,
    val isHighContrastModeEnabled: Boolean = false,
    val themeType: ThemeType = ThemeType.System
) {

    val isDarkTheme: Boolean
        @Composable get(){
            val darkModeStatus = when(darkMode) {
                DarkTheme.System -> isSystemInDarkTheme()
                DarkTheme.On -> true
                DarkTheme.Off -> false
            }
            return darkModeStatus
        }

    fun Context.getDarkThemeDesc(): String {
        return when (darkMode) {
            DarkTheme.System -> getString(R.string.follow_system)
            DarkTheme.On -> getString(R.string.on)
            DarkTheme.Off -> getString(R.string.off)
        }
    }
}
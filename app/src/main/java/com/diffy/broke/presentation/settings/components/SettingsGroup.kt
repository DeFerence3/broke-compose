package com.diffy.broke.presentation.settings.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import com.diffy.broke.BuildConfig
import com.diffy.broke.presentation.core.ui.theme.conf.DarkTheme
import com.diffy.broke.presentation.core.ui.theme.conf.ThemeType
import com.diffy.settings.ui.core.SettingsExt

data class SettingsGroup(
    val header: String,
    val settingsItems: List<SettingsExt<*>>
){
    companion object {
        fun getSettings(appSettings: SettingsSaver.AppSettings, isDark: Boolean): List<SettingsGroup> {

            val developerOptionSetting = SettingsExt.SwitchSetting(
                icon = Icons.Default.Build,
                title = "Developer Mode",
                summary = "Enable to use developer options",
                enabled = SettingsSaver.isADeveloper(),
                value = appSettings.isDeveloperMode,
                onCheck = { SettingsSaver.toggleDeveloperMode(enabled = it) }
            )

            val themeSetting = SettingsExt.MultiOptionSetting(
                icon = Icons.Default.Face,
                title = "Theme",
                summary = "Switch Between Themes",
                enabled = true,
                options = ThemeType.entries,
                selectedOption = appSettings.themePreference.themeType,
                onOptionSelected = { SettingsSaver.switchTheme(it)}
            )

            val darkModeSetting = SettingsExt.MultiOptionSetting(
                icon = Icons.Default.DarkMode,
                title = "Dark Mode",
                summary = "Dark Mode Preference",
                enabled = true,
                options = DarkTheme.entries,
                selectedOption = appSettings.themePreference.darkMode,
                onOptionSelected = { SettingsSaver.switchThemeMode(it) }
            )

            val highContrastSetting = SettingsExt.SwitchSetting(
                icon = Icons.Default.Album,
                title = "High Contrast",
                summary = "Enable to use High Contrast",
                enabled = isDark,
                value = appSettings.themePreference.isHighContrastModeEnabled,
                onCheck = { SettingsSaver.toggleHighContrastMode(enabled = it) },
            )

            val versionSetting = SettingsExt.InfoSetting(
                icon = Icons.Default.Info,
                title = "Version",
                summary = null,
                enabled = true,
                value = BuildConfig.VERSION_NAME
            )

            return listOf(
                SettingsGroup(
                    header = "Appearance",
                    settingsItems = listOf(
                        themeSetting,
                        darkModeSetting,
                        highContrastSetting
                    )
                ),
                SettingsGroup(
                    header = "About",
                    settingsItems = listOf(
                        versionSetting
                    )
                )
            )

        }
    }
}
package com.diffy.broke.presentation.settings.components

import com.diffy.broke.Broke.Companion.applicationScope
import com.diffy.broke.presentation.core.ui.theme.ThemePreference
import com.diffy.broke.presentation.core.ui.theme.conf.DarkTheme
import com.diffy.broke.presentation.core.ui.theme.conf.ThemeType
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val THEME_MODE = "theme_mode"
private const val HIGH_CONTRAST = "high_contrast"
private const val DEVELOPER_MODE = "developer_mode"
private const val THEME_TYPE = "theme_type"
private const val IS_A_DEVELOPER = "is_a_developer"

object SettingsSaver {
    private val kv: MMKV = MMKV.defaultMMKV()

    private val mutableAppSettingsStateFlow =
        MutableStateFlow(
            AppSettings(
                themePreference = ThemePreference(
                    darkMode = getDarkMode(),
                    isHighContrastModeEnabled = kv.decodeBool(HIGH_CONTRAST, false),
                    themeType = getThemeType()
                ),
                isDeveloperMode = kv.decodeBool(DEVELOPER_MODE, false),
            )
        )

    val AppSettingsStateFlow = mutableAppSettingsStateFlow.asStateFlow()

    fun switchTheme(
        themeType: ThemeType = AppSettingsStateFlow.value.themePreference.themeType
    ) {
        applicationScope.launch(Dispatchers.IO) {
            mutableAppSettingsStateFlow.update { it.copy(themePreference = AppSettingsStateFlow.value.themePreference.copy(themeType = themeType)) }
            putThemeType(themeType)
        }
    }

    fun switchThemeMode(darkTheme: DarkTheme = AppSettingsStateFlow.value.themePreference.darkMode) {
        applicationScope.launch(Dispatchers.IO) {
            mutableAppSettingsStateFlow.update { state ->
                state.copy(
                    themePreference = state.themePreference.copy(
                        darkMode = darkTheme,
                        isHighContrastModeEnabled = if (darkTheme == DarkTheme.Off) false else state.themePreference.isHighContrastModeEnabled
                    )
                )
            }
            putDarkMode(darkTheme)
        }
    }


    fun toggleDeveloperMode(enabled: Boolean) {
        applicationScope.launch(Dispatchers.IO) {
            mutableAppSettingsStateFlow.update { it.copy(isDeveloperMode = enabled) }
            kv.encode(DEVELOPER_MODE,enabled)
            if (!enabled){
                setDeveloper(false)
            }
        }
    }

    fun toggleHighContrastMode(enabled: Boolean) {
        applicationScope.launch(Dispatchers.IO) {
            mutableAppSettingsStateFlow.update { it.copy(themePreference = AppSettingsStateFlow.value.themePreference.copy(isHighContrastModeEnabled = enabled)) }
            kv.encode(HIGH_CONTRAST,enabled)
        }
    }

    //mmkv utils
    private fun putThemeType(themeType: ThemeType) = kv.encode(THEME_TYPE,themeType.name)
    private fun putDarkMode(darkTheme: DarkTheme) = kv.encode(THEME_MODE,darkTheme.name)

    private fun getThemeType() = try {
        kv.decodeString(THEME_TYPE)?.let { ThemeType.valueOf(it) } ?: ThemeType.System
    } catch (e: Exception) {
        ThemeType.System
    }

    private fun getDarkMode() = try {
        kv.decodeString(THEME_MODE)?.let { DarkTheme.valueOf(it) } ?: DarkTheme.System
    } catch (e: Exception){
        DarkTheme.System
    }

    fun isADeveloper() = kv.decodeBool(IS_A_DEVELOPER)
    fun setDeveloper(isDeveloper: Boolean) {
        kv.putBoolean(IS_A_DEVELOPER,isDeveloper)
        if (!isDeveloper){
            kv.encode(DEVELOPER_MODE,false)
        }
    }

    data class AppSettings(
        val themePreference: ThemePreference,
        val isDeveloperMode: Boolean
    )

}
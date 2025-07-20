package com.diffy.settings.ui.core

import androidx.compose.ui.graphics.vector.ImageVector

sealed class SettingsExt<T>(
    open val icon: ImageVector? = null,
    open val title: String,
    open val summary: String? = null,
    val type: SettingsType = SettingsType.Info,
    open val enabled: Boolean = true,
    open val value: T,
    open val onValueChange: ((T) -> Unit)? = null
){
    data class InfoSetting(
        override val icon: ImageVector? = null,
        override val title: String,
        override val summary: String? = null,
        override val enabled: Boolean = true,
        override val value: String
    ) : SettingsExt<String>(
        icon = icon,
        title = title,
        summary = summary,
        enabled = enabled,
        type = SettingsType.Info,
        value = value
    )

    data class SwitchSetting(
        override val icon: ImageVector? = null,
        override val title: String,
        override val summary: String? = null,
        override val enabled: Boolean = true,
        override val value: Boolean = false,
        val onCheck: ((Boolean) -> Unit)? = null,
    ) : SettingsExt<Boolean>(
        icon = icon,
        title = title,
        summary = summary,
        enabled = enabled,
        value = value,
        type = SettingsType.Switch,
        onValueChange = onCheck
    )

    data class MultiOptionSetting<T>(
        override val icon: ImageVector? = null,
        override val title: String,
        override val summary: String? = null,
        override val enabled: Boolean = true,
        val options: List<T>,
        val selectedOption: T,
        val onOptionSelected: (T) -> Unit
    ) : SettingsExt<T>(
        icon = icon,
        title = title,
        summary = summary,
        enabled = enabled,
        type = SettingsType.MultiOption(options),
        value = selectedOption,
        onValueChange = onOptionSelected
    )
}

sealed interface SettingsType {
    data object Switch : SettingsType
    data object Info : SettingsType
    data class MultiOption<T>(val items: List<T>) : SettingsType
}
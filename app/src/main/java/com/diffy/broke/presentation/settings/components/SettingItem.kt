package com.diffy.broke.presentation.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.diffy.settings.ui.core.SettingsExt
import com.diffy.settings.ui.core.SettingsType
import com.diffy.settings.ui.settings.components.ItemPosition

@Composable
fun <T> SettingItem(
    modifier: Modifier = Modifier,
    item: SettingsExt<T>,
    itemPosition: ItemPosition
) {

    val trailingContent: (@Composable () -> Unit) = getTrailingContent(item)

    val icon: @Composable (() -> Unit)? = item.icon?.let {
        { Icon(imageVector = it, contentDescription = "Icon") }
    }

    val supportingContent: @Composable (() -> Unit)? = item.summary?.let {
        { Text(text = it) }
    }

    val paddingModifier = getPadding(itemPosition,false)

    val shape = getShape(itemPosition)

    val alpha = if (item.enabled) 1f else 0.4f

    ListItem(
        modifier = Modifier
            .then(paddingModifier)
            .padding(horizontal = 16.dp)
            .clip(shape)
            .background(
                color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
            )
            .padding(8.dp)
            .fillMaxWidth()
            .alpha(alpha)
            .then(modifier),
        headlineContent = {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 2.dp)
            )
        },
        supportingContent = supportingContent,
        trailingContent = trailingContent,
        leadingContent = icon,
        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
    )
}

@Composable
fun ContentedSettingItem(
    modifier: Modifier = Modifier,
    title: String,
    summary: String?,
    value: String,
    itemPosition: ItemPosition,
    onClick: (() -> Unit)?,
    isZeroPadded: Boolean = false
) {

    val shape = getShape(itemPosition)

    val paddingModifier = getPadding(itemPosition,isZeroPadded)

    val item = SettingsExt.InfoSetting(
        icon = null,
        title = title,
        summary = summary,
        enabled = true,
        value = value
    )

    val trailingContent: (@Composable () -> Unit) = getTrailingContent(item)

    val icon: @Composable (() -> Unit)? = item.icon?.let {
        { Icon(imageVector = it, contentDescription = "Icon") }
    }

    val supportingContent: @Composable (() -> Unit)? = item.summary?.let {
        { Text(text = it) }
    }

    val alpha = if (item.enabled) 1f else 0.4f

    ListItem(
        modifier = Modifier

            .then(paddingModifier)
            .padding(horizontal = 16.dp)
            .clip(shape)
            .clickable(onClick != null){
                onClick?.invoke()
            }
            .background(
                color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
            )
            .padding(8.dp)
            .fillMaxWidth()
            .alpha(alpha)
            .then(modifier),
        headlineContent = {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 2.dp)
            )
        },
        supportingContent = supportingContent,
        trailingContent = trailingContent,
        leadingContent = icon,
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
    )
}


// some common functions
fun getPadding(itemPosition: ItemPosition, isZeroPadded: Boolean = false) =
    if (isZeroPadded) Modifier.padding(0.dp)
    else when (itemPosition) {
        ItemPosition.Alone -> Modifier.padding(bottom = 16.dp)
        ItemPosition.Bottom -> Modifier.padding(top = 1.dp, bottom = 16.dp)
        ItemPosition.Middle -> Modifier.padding(vertical = 1.dp)
        ItemPosition.Top -> Modifier.padding(bottom = 1.dp)
    }


fun getShape(itemPosition: ItemPosition) =
    when (itemPosition) {
        ItemPosition.Alone -> RoundedCornerShape(24.dp)
        ItemPosition.Middle -> RoundedCornerShape(4.dp)
        ItemPosition.Bottom -> RoundedCornerShape(
            topStart = 4.dp,
            topEnd = 4.dp,
            bottomStart = 24.dp,
            bottomEnd = 24.dp
        )
        ItemPosition.Top -> RoundedCornerShape(
            topStart = 24.dp,
            topEnd = 24.dp,
            bottomStart = 4.dp,
            bottomEnd = 4.dp
        )
    }

fun <T> getTrailingContent(item: SettingsExt<T>): (@Composable () -> Unit){
    return when (item.type) {
        SettingsType.Switch -> {
            {
                Switch(
                    checked = item.value as Boolean,
                    onCheckedChange = { item.onValueChange?.invoke(it as T) },
                    enabled = item.enabled
                )
            }
        }
        SettingsType.Info -> {
            { Text(item.value.toString()) }
        }
        is SettingsType.MultiOption<*> -> {
            {
                var expanded by remember { mutableStateOf(false) }
                Box {
                    TextButton(
                        onClick = { expanded = true },
                        enabled = item.enabled
                    ) {
                        Text(text = item.value.toString())
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        item.type.items.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.toString()) },
                                onClick = {
                                    item.onValueChange?.invoke(option as T)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
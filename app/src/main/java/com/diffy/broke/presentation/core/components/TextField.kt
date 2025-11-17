package com.diffy.broke.presentation.core.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ClickableTextField(
    value: String,
    onClick: () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = { },
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth(),
        leadingIcon = leadingIcon,
        enabled = false,
        label = {
            Text(text = label)
        },
        colors = OutlinedTextFieldDefaults.colors().copy(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledIndicatorColor = MaterialTheme.colorScheme.outline,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}
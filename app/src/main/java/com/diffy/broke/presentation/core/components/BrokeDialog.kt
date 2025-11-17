package com.diffy.broke.presentation.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

/***
 *
 * @param onNegativeAction action (Dismiss, Cancel, No, etc), Called also when clicking outside the dialog
 * @param onPositiveAction action (Submit, Ok, Yes, etc)
 * @param content content shown
 */
@Composable
fun BrokeDialog(
    modifier: Modifier = Modifier,
    title: String,
    onPositiveAction: () -> Unit,
    positiveText: String,
    negativeText: String,
    onNegativeAction: () -> Unit,
    positiveButtonEnabled: Boolean = true,
    negativeButtonEnabled: Boolean = true,
    content: @Composable (ColumnScope.() -> Unit)
) {
    AlertDialog(
        modifier = modifier
            .padding(16.dp),
        onDismissRequest = onNegativeAction,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        ),
        title = { Text(text = title) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                content(this)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onNegativeAction,
                enabled = negativeButtonEnabled
            ) {
                Text(text = negativeText)
            }
        },
        confirmButton = {
            Button(
                onClick = onPositiveAction,
                enabled = positiveButtonEnabled
            ) {
                Text(text = positiveText)
            }
        },
    )
}

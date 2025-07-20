package com.diffy.broke.presentation.core.templates

import androidx.compose.runtime.Composable

@Composable
inline fun Boolean.OnShowDialog(dialog: @Composable () -> Unit) {
    if (this) {
        dialog.invoke()
    }
}
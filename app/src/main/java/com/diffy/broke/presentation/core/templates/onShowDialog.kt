package com.diffy.broke.presentation.core.templates

import androidx.compose.runtime.Composable

@Composable
inline fun Boolean.OnShowDialog(dialog: @Composable () -> Unit) =
    if (this) dialog.invoke() else Unit

@Composable
inline fun <T> T?.OnShowDialog(dialog: @Composable (T) -> Unit) =
    if (this != null) dialog.invoke(this) else Unit
package com.diffy.broke.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun RestartDialog() {
    AlertDialog(
        onDismissRequest = { /*TODO*/ },
        title = { Text(text = "Restart") },
        confirmButton = {
            TextButton(
                onClick = { /*TODO*/ }
            ) {
                Text(text = "Restart")
            }
        },
    )
}
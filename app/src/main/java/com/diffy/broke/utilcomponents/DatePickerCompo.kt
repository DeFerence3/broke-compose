package com.diffy.broke.utilcomponents

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun datePickerScreen(
    datePickerState: DatePickerState,
    onShowDialogChange: (Boolean) -> Unit
): Long {
    var selectedDate: Long = datePickerState.selectedDateMillis!!
    DatePickerDialog(
        onDismissRequest = { onShowDialogChange(false) },
        confirmButton = {
            TextButton(onClick = {
                onShowDialogChange(false)
                selectedDate = datePickerState.selectedDateMillis!!
            }) {
                Text("Ok")
            }
        },
        dismissButton = {
            TextButton(onClick = { onShowDialogChange(false) }) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
    return selectedDate
}
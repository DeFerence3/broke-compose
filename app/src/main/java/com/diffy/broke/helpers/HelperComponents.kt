package com.diffy.broke.helpers

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerScreen(
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

@Composable
fun DateInMillisToFormat(dateMillis: Long?): String {
    val currentDate = Calendar.getInstance()
    val selectedDate = Calendar.getInstance().apply {
        if (dateMillis != null) {
            timeInMillis = dateMillis
        }
    }

    return when {
        currentDate.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR) &&
                currentDate.get(Calendar.DAY_OF_YEAR) == selectedDate.get(Calendar.DAY_OF_YEAR) -> {
            "Today"
        }
        else -> {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dateFormat.format(selectedDate.time)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerScreen(
    dateRangePickerState: DateRangePickerState,
    onDismiss: () -> Unit,
) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Top
        ) {
            // Add a row with "Save" and dismiss actions.

            DateRangePicker(state = dateRangePickerState, modifier = Modifier.weight(1f))
        }
    }
}
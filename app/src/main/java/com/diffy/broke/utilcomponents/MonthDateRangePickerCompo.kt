package com.diffy.broke.utilcomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.diffy.broke.ui.monthpicker.MonthPicker
import com.diffy.broke.ui.monthpicker.MonthPickerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthPickerCompo(
    modifier: Modifier = Modifier,
    monthPicked: () -> Unit,
    cancelClicked: () -> Unit,
    dateRangePickerState: DateRangePickerState,
    dateRangePicked: () -> Unit,
    monthPickerState: MonthPickerState
) {
    var isDatePickerShowing by remember { mutableStateOf(false) }
    if (isDatePickerShowing) {
        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = { isDatePickerShowing = !isDatePickerShowing }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.Top
            ) {
                DateRangePicker(state = dateRangePickerState, modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = {
                        isDatePickerShowing = !isDatePickerShowing
                    }) {
                        Text(text = "Month Picker")
                    }
                    Row {
                        TextButton(onClick = { isDatePickerShowing = !isDatePickerShowing }) {
                            Text(text = "Cancel")
                        }
                        TextButton(onClick = {
                            dateRangePicked()
                            cancelClicked()
                        }) {
                            Text(text = "Set")
                        }
                    }
                }
            }
        }
    } else {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = {
                cancelClicked()
            },
            text = {
                MonthPicker(monthPickerState = monthPickerState)
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween ) {
                    TextButton(onClick = {
                        isDatePickerShowing = !isDatePickerShowing
                    }) {
                        Text(text = "Date Range Picker")
                    }
                    TextButton(onClick = {
                        monthPicked()
                        cancelClicked()
                    }) {
                        Text(text = "Set")
                    }
                }
            }
        )
    }
}
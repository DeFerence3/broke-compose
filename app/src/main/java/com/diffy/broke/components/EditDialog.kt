package com.diffy.broke.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.diffy.broke.Events
import com.diffy.broke.States
import com.diffy.broke.helpers.DatePickerScreen
import com.diffy.broke.helpers.DateInMillisToFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPackDialog(
    state: States,
    onEvent: (Events) -> Unit,
    modifier: Modifier = Modifier
) {

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = state.transactionDateInMillis
    )
    var showDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }

    if (showDialog){
        selectedDate = DatePickerScreen(
            datePickerState = datePickerState,
            onShowDialogChange = { showDialog = it }
        )
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(Events.HideEditDialog)
        },
        title = { Text(text = "Edit Transaction") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                var isExp by remember { mutableStateOf(state.isExp) }
                onEvent(Events.SetExpInc(isExp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    FilterChip(
                        selected = false,
                        onClick = {
                            showDialog = !showDialog
                        },
                        label = { Text(text = DateInMillisToFormat(datePickerState.selectedDateMillis)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.EditCalendar,
                                contentDescription = "Date",
                            )
                        },
                    )
                    FilterChip(
                        selected = false,
                        onClick = {
                            isExp = !isExp
                        },
                        label = { Text(text = if (isExp) "Expense" else "Income") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Loop,
                                contentDescription = "Toggle Income Expense",
                            )
                        },
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = if (isExp) Color.Red else Green
                        )
                    )
                }

                OutlinedTextField(
                    value = state.transactionName,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AccountBalance,
                            contentDescription = "Transaction"
                        )
                    },
                    onValueChange = {
                        onEvent(Events.SetTransactionName(it))
                    },
                    placeholder = {
                        Text(text = "Transaction")
                    }
                )

                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp))

                OutlinedTextField(
                    value = state.transactionAmount,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.CurrencyRupee,
                            contentDescription = "Transaction"
                        )
                    },
                    onValueChange = {
                        onEvent(Events.SetAmount(it))
                    },
                    placeholder = {
                        Text(text = "Amount")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onEvent(Events.SetTransactionDate(selectedDate))
                    onEvent(Events.CreateTransaction)
                }
            ) {
                Text(text = "Edit")
            }
        },
    )
}
package com.diffy.broke.presentation.transaction.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.diffy.broke.R
import com.diffy.broke.domain.model.Transaction
import com.diffy.broke.domain.model.TransactionFormModes
import com.diffy.broke.presentation.core.templates.OnShowDialog
import com.diffy.broke.presentation.core.ui.components.ClickableTextField
import com.diffy.broke.presentation.core.ui.util.dateInMillisToFormat
import com.diffy.broke.presentation.core.ui.util.datePickerScreen
import com.diffy.broke.presentation.transaction.TransactionEvents

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditDialog(
    modifier: Modifier = Modifier,
    onEvent: (TransactionEvents) -> Unit,
    onSelectClick: () -> Unit,
    selectedTransaction: Transaction
) {
    var selectedDate by remember { mutableLongStateOf(selectedTransaction.day) }
    var amount by remember { mutableStateOf(selectedTransaction.amount) }
    var notes by remember { mutableStateOf(selectedTransaction.notes) }
    var category by remember { mutableStateOf(selectedTransaction.category) }
    var isIncome by remember { mutableStateOf(selectedTransaction.isIncome) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val formFocusRequesters = List(4) { FocusRequester() }
    var showDatetimepicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState( initialSelectedDateMillis = selectedDate )

    showDatetimepicker.OnShowDialog{
        selectedDate = datePickerScreen(
            datePickerState = datePickerState,
            onShowDialogChange = { showDatetimepicker = it }
        )
    }

    AlertDialog(
        modifier = modifier
            .padding(16.dp),
        onDismissRequest = {
            onEvent(TransactionEvents.HideAddEditDialog)
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        ),
        title = { Text(text = if (selectedTransaction.mode == TransactionFormModes.EDIT) stringResource(R.string.edit_transaction) else stringResource(R.string.add_transaction)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                //Filter Chip Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FilterChip(
                        selected = false,
                        onClick = { showDatetimepicker = !showDatetimepicker },
                        label = { Text(text = dateInMillisToFormat(selectedDate)) },
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
                            isIncome = !isIncome
                        },
                        label = { Text(text = if (isIncome) "Income" else "Expense") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Loop,
                                contentDescription = "Toggle Income Expense",
                            )
                        },
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = if (isIncome) Color.Green else Color.Red,
                            enabled = true,
                            selected = false
                        )
                    )
                }
                ClickableTextField(
                    value = category.categoryName,
                    onClick = onSelectClick,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AccountBox,
                            contentDescription = stringResource(R.string.category)
                        )
                    },
                    label = "From Account"
                )

                OutlinedTextField(
                    modifier = Modifier
                        .focusRequester(formFocusRequesters[2])
                        .fillMaxWidth(),
                    value = selectedTransaction.amount,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.CurrencyRupee,
                            contentDescription = stringResource(R.string.amount)
                        )
                    },
                    onValueChange = {
                        amount = it
                        onEvent(TransactionEvents.SetSelectedTransaction(selectedTransaction.copy(amount = it)))
                    },
                    label = {
                        Text(text = stringResource(R.string.amount))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            formFocusRequesters[3].requestFocus()
                        }
                    )
                )
                OutlinedTextField(
                    modifier = Modifier
                        .focusRequester(formFocusRequesters[3])
                        .height(100.dp)
                        .fillMaxWidth(),
                    value = selectedTransaction.notes,
                    onValueChange = {
                        onEvent(TransactionEvents.SetSelectedTransaction(selectedTransaction.copy(notes = it)))
                    },
                    label = {
                        Text(text = "Notes")
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val transaction = Transaction(
                        id = selectedTransaction.id,
                        amount = amount,
                        day = selectedDate,
                        isIncome = isIncome,
                        notes = notes,
                        category = category,
                    )
                    onEvent(TransactionEvents.CreateTransaction(transaction))
                }
            ) {
                Text(text = if (selectedTransaction.mode == TransactionFormModes.ADD) stringResource(R.string.add) else stringResource(R.string.edit))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onEvent(TransactionEvents.HideAddEditDialog)
                }
            ) {
                Text(text = stringResource(R.string.dismiss))
            }
        }
    )
}
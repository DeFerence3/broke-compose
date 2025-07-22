@file:OptIn(ExperimentalTime::class)

package com.diffy.broke.presentation.transaction.components

import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.diffy.broke.R
import com.diffy.broke.domain.model.AccountHead
import com.diffy.broke.domain.model.Transaction
import com.diffy.broke.presentation.core.search.SearchContract
import com.diffy.broke.presentation.core.ui.components.ClickableTextField
import com.diffy.broke.presentation.core.ui.util.dateInMillisToFormat
import com.diffy.broke.presentation.core.ui.util.datePickerScreen
import com.diffy.broke.presentation.transaction.TransactionEvents
import com.diffy.broke.presentation.transaction.TransactionStates
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditDialog(
    modifier: Modifier = Modifier,
    state: TransactionStates,
    onEvent: (TransactionEvents) -> Unit,
) {
    val selectedTransaction = state.selectedTransaction
    var showDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableLongStateOf(selectedTransaction?.day ?: System.currentTimeMillis()) }
    val datePickerState = rememberDatePickerState( initialSelectedDateMillis = selectedDate )
    var amount by remember { mutableStateOf(selectedTransaction?.amount ?: "") }
    var transactionTitle by remember { mutableStateOf(selectedTransaction?.notes ?: "") }
    var fromAccount by remember { mutableStateOf(selectedTransaction?.fromAccountHead) }
    var toAccount by remember { mutableStateOf(selectedTransaction?.toAccountHead) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val fromAccountSelector =
        rememberLauncherForActivityResult(
            contract = SearchContract(AccountHead::class),
            onResult = { item ->
                fromAccount = item
            })

    val toAccountSelector =
        rememberLauncherForActivityResult(
            contract = SearchContract(AccountHead::class),
            onResult = { item ->
                toAccount = item
            })

    val formFocusRequesters = List(4) { FocusRequester() }

    if (showDialog) {
        selectedDate = datePickerScreen(
            datePickerState = datePickerState,
            onShowDialogChange = { showDialog = it }
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
        title = { Text(text = if (selectedTransaction?.id != null) stringResource(R.string.edit_transaction) else stringResource(R.string.add_transaction)) },
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
                        onClick = { showDialog = !showDialog },
                        label = { Text(text = dateInMillisToFormat(selectedDate)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.EditCalendar,
                                contentDescription = "Date",
                            )
                        },
                    )
                }
                ClickableTextField(
                    value = fromAccount?.accountHeadName ?: "",
                    onClick = { fromAccountSelector.launch(Unit) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AccountBox,
                            contentDescription = stringResource(R.string.accounthead)
                        )
                    },
                    label = "From Account"
                )

                ClickableTextField(
                    value = toAccount?.accountHeadName ?: "",
                    onClick = {
                        toAccountSelector.launch(Unit)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AccountBox,
                            contentDescription = stringResource(R.string.accounthead)
                        )
                    },
                    label = "To Account"
                )
                OutlinedTextField(
                    modifier = Modifier
                        .focusRequester(formFocusRequesters[2])
                        .fillMaxWidth(),
                    value = amount,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.CurrencyRupee,
                            contentDescription = stringResource(R.string.amount)
                        )
                    },
                    onValueChange = { amount = it },
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
                    value = transactionTitle,
                    onValueChange = { transactionTitle= it },
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
                    val transaction = castToTransaction(
                        id = selectedTransaction?.id ?: 0,
                        title = transactionTitle,
                        amount = amount,
                        day = selectedDate,
                        fromAccount = fromAccount!!,
                        toAccount = toAccount!!
                    )
                    onEvent(TransactionEvents.CreateTransaction(transaction))
                }
            ) {
                Text(text = if (selectedTransaction?.id != null) stringResource(R.string.add) else stringResource(R.string.edit))
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

fun castToTransaction(
    id: Int,
    title: String,
    amount: String,
    day: Long,
    fromAccount: AccountHead,
    toAccount: AccountHead
): Transaction = Transaction(
    id = id,
    notes = title,
    amount = amount,
    day = day,
    fromAccountHead = fromAccount,
    toAccountHead = toAccount
)
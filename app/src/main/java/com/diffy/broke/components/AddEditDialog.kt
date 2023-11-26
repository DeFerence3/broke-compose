package com.diffy.broke.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.diffy.broke.state.Events
import com.diffy.broke.state.States
import com.diffy.broke.ViewModel
import com.diffy.broke.database.Tags
import com.diffy.broke.ui.CustomTextField
import com.diffy.broke.utilcomponents.dateInMillisToFormat
import com.diffy.broke.utilcomponents.datePickerScreen
import com.diffy.broke.utilcomponents.getTimeInMillisWithGivenDay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditPackDialog(
    state: States,
    onEvent: (Events) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableLongStateOf( if (state.isEditingTransaction) state.transactionDateInMillis else System.currentTimeMillis()) }
    val datePickerState = rememberDatePickerState( initialSelectedDateMillis = selectedDate )
    var tags by remember { mutableStateOf<List<Tags>>(emptyList()) }
    var text by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var buttontext by remember { mutableStateOf("") }
    var isExp by remember { mutableStateOf(true) }

    if (state.isEditingTransaction) {
        isExp = state.isExp
        title = "Edit Transaction"
        buttontext = "Edit"
        onEvent(Events.SetId(state.id))
        tags = state.tags
    } else {
        title = "Add Transaction"
        buttontext = "Add"
    }

    if (showDialog) {
        selectedDate = datePickerScreen(
            datePickerState = datePickerState,
            onShowDialogChange = { showDialog = it }
        )
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(Events.HideAddDialog)
            onEvent(Events.HideEditDialog)
            viewModel.clearState()
        },
        title = { Text(text = title) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
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
                            enabled = true,
                            borderColor = if (isExp) Color.Red else Green,
                            selected = false
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
                    label = {
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
                    label = {
                        Text(text = "Amount")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
                FlowRow(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    tags.forEach{ tag ->
                        SuggestionChip(
                            onClick = { tags.toMutableList().apply {
                                tags -= tag
                            } },
                            label = { Text("#${tag.tag}") },
                            modifier = Modifier.height(30.dp),
                        )
                    }
                }
                FlowRow(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    if (tags.size <= 4) {
                        CustomTextField(
                            value = text,
                            onValueChange = { text = it },
                            keyboardactions = KeyboardActions (
                                onGo = {
                                    if(text != "") {
                                        tags += Tags(0,text)
                                        onEvent(Events.SetTags(tags))
                                        text = ""
                                    }
                                }
                            ),
                            keyboardoptions = KeyboardOptions(
                                imeAction = ImeAction.Go
                            )
                        )
                    }
                    if (text != ""){
                        viewModel.getTagsByTag(text).collectAsState(initial = null).value?.forEach { tag->
                            SuggestionChip(
                                onClick = {
                                    tags += tag
                                    onEvent(Events.SetTags(tags))
                                    text = ""
                                },
                                label = { Text("#${tag.tag}") },
                                modifier = Modifier.height(30.dp),
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onEvent(Events.SetExpInc(isExp))
                    onEvent(Events.SetTransactionDate(getTimeInMillisWithGivenDay(selectedDate)))
                    onEvent(Events.CreateTransaction)
                }
            ) {
                Text(text = buttontext)
            }
        },
    )
}
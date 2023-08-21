package com.diffy.broke.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.diffy.broke.Events
import com.diffy.broke.States

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPackDialog(
    state: States,
    onEvent: (Events) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(Events.HideDialog)
        },
        title = { Text(text = "New Transaction") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                var isExp by remember { mutableStateOf(true) }

                FilterChip(
                    selected = false,
                    onClick = {
                        isExp = !isExp
                        onEvent(Events.SetExpInc(isExp))
                     },
                    label = { Text(text = if (isExp) "Expense" else "Income") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Loop,
                            contentDescription = "Toggle Income Expense",
                        )
                    }
                )

                OutlinedTextField(
                    value = state.transactionName,
                    onValueChange = {
                        onEvent(Events.SetGroupName(it))
                    },
                    placeholder = {
                        Text(text = "Transaction")
                    }
                )

                OutlinedTextField(
                    value = state.transactionAmount,
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
                    onEvent(Events.CreateGroup)
                }
            ) {
                Text(text = "Add")
            }
        },
    ) 
}
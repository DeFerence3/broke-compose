package com.diffy.broke.presentation.accounthead

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.diffy.broke.R
import com.diffy.broke.domain.model.AccountHead
import com.diffy.broke.presentation.core.slidingdrawer.SlidingDrawerState
import com.diffy.broke.presentation.core.templates.ScaffoldTemplate
import com.diffy.broke.presentation.core.ui.components.DropDownSelector
import com.diffy.broke.presentation.core.ui.util.ObserveEvent
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountHeadScreen(
    onEvent: (AccountHeadEvent) -> Unit,
    state: AccountHeadState,
    oneTimeEventChannelFlow: Flow<AccountHeadOneTimeEvent>,
    drawerClick: (SlidingDrawerState) -> Unit,
    drawerState: SlidingDrawerState,
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    // Observe one-time events, like showing a Toast
    oneTimeEventChannelFlow.ObserveEvent {
        when (it) {
            is AccountHeadOneTimeEvent.Success -> {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
            is AccountHeadOneTimeEvent.Error -> {
                Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Dialog for adding or editing an account Head
    if (state.isAddOrEditDialogShowing) {
        Dialog(
            onDismissRequest = { onEvent(AccountHeadEvent.HideAddOrEditDialog) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(androidx.compose.material3.MaterialTheme.colorScheme.surface, androidx.compose.foundation.shape.RoundedCornerShape(8.dp)), // Added background and shape
                verticalArrangement = Arrangement.spacedBy(16.dp), // Increased spacing
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally // Center dialog content
            ) {
                Text(
                    text = if (state.selectedAccountHead?.id == null) "Add Account Head" else "Edit Account Head",
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge
                )
                OutlinedTextField(
                    value = state.selectedAccountHead?.accountHeadName ?: "",
                    onValueChange = { newValue ->
                        val updatedAccountHead = state.selectedAccountHead?.copy(accountHeadName = newValue)
                            ?: AccountHead(id = 0,accountHeadName = newValue, accountGroup = null)
                        onEvent(AccountHeadEvent.SelectAccountHead(updatedAccountHead))
                    },
                    label = { Text("Account Head Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                DropDownSelector(
                    expanded = expanded,
                    value = { it?.accountGroupName },
                    onSearch = { newValue ->
                        onEvent(AccountHeadEvent.SearchAccountGroup(newValue))
                    },
                    onExpandedChange = { expanded = it },
                    listItems = state.groups,
                    label = "Account Group",
                    selectedItem = state.selectedAccountHead?.accountGroup,
                    onSelect = { newValue ->
                        val newAccountHead = state.selectedAccountHead?.copy(accountGroup = newValue) ?: AccountHead(id = 0,accountHeadName = "", accountGroup = newValue)
                        onEvent(AccountHeadEvent.SelectAccountHead(newAccountHead))
                    }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { onEvent(AccountHeadEvent.HideAddOrEditDialog) },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(text = "Cancel")
                    }
                    Button(
                        onClick = { onEvent(AccountHeadEvent.SaveAccountHead) },
                        enabled = !state.selectedAccountHead?.accountHeadName.isNullOrBlank() // Enable only if name is not blank
                    ) {
                        Text(text = "Submit")
                    }
                }
            }
        }
    }

    // Main screen content
    ScaffoldTemplate(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.account_Head)) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            drawerClick(drawerState.opposite())
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = stringResource(R.string.navigation_icon))
                    }
                }
            )
        },
        floatingActionButtonText = "Add",
        floatingActionButtonAction = {
            onEvent(AccountHeadEvent.AddAccountHead)
        }
    ) { padding ->
        Column( // Use Column to manage loading state
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (state.isLoading) {
                androidx.compose.material3.CircularProgressIndicator()
                Text("Loading account Heads...")
            } else if (state.accountHeads.isEmpty()) {
                Text("No account Heads found. Tap 'Add' to create one.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(state.accountHeads, key = { it.id }) { head -> // Added key for better recomposition
                        AccountHeadItem(head = head) {
                            // Handle item click for editing
                            onEvent(AccountHeadEvent.SelectAccountHead(head))
                            onEvent(AccountHeadEvent.AddAccountHead) // Show dialog for editing
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AccountHeadItem(head: AccountHead, onClick: (AccountHead) -> Unit) {
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(head) }, // Made card clickable
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = androidx.compose.material3.MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = head.accountHeadName,
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
            )
            // You can add more details here if AccountGroup has more properties
            // Text(text = "Accounts: ${group.accounts.size}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
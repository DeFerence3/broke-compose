package com.diffy.broke.presentation.accounthead

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.diffy.broke.R
import com.diffy.broke.domain.model.AccountGroup
import com.diffy.broke.domain.model.AccountHead
import com.diffy.broke.presentation.core.search.SearchContract
import com.diffy.broke.presentation.core.slidingdrawer.SlidingDrawerState
import com.diffy.broke.presentation.core.templates.ScaffoldTemplate
import com.diffy.broke.presentation.core.ui.components.BrokeDialog
import com.diffy.broke.presentation.core.ui.components.ClickableTextField
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
    val accountGroupSelector =
        rememberLauncherForActivityResult(
            contract = SearchContract(AccountGroup::class),
            onResult = { item ->
                item.let {
                    val updatedAccountHead = state.selectedAccountHead?.copy(accountGroup = it)
                    if(updatedAccountHead != null) {
                        onEvent(AccountHeadEvent.SelectAccountHead(updatedAccountHead))
                    }
                }
            })

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

    if (state.selectedAccountHead != null) {
        BrokeDialog(
            onNegativeAction = { onEvent(AccountHeadEvent.HideAddOrEditDialog) },
            onPositiveAction = { onEvent(AccountHeadEvent.SaveAccountHead) },
            positiveButtonEnabled = state.selectedAccountHead.accountHeadName.isNotBlank(),
            title = if (state.selectedAccountHead.id == 0) "Add Account Head" else "Edit Account Head",
            positiveText = "Submit",
            negativeText = "Cancel"
        ) {
            OutlinedTextField(
                value = state.selectedAccountHead.accountHeadName,
                onValueChange = { newValue ->
                    val updatedAccountHead = state.selectedAccountHead.copy(accountHeadName = newValue)
                    onEvent(AccountHeadEvent.SelectAccountHead(updatedAccountHead))
                },
                label = { Text("Account Head Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            ClickableTextField(
                value = state.selectedAccountHead.accountGroup?.accountGroupName ?: "",
                onClick = {
                    accountGroupSelector.launch(Unit)
                },
                label = "Account Group",
            )
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
                Text("Loading account Heads...")
            } else if (state.accountHeads.isEmpty()) {
                Text("No account Heads found. Tap 'Add' to create one.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(state.accountHeads, key = { it.id }) { head ->
                        AccountHeadItem(head = head) {
                            onEvent(AccountHeadEvent.SelectAccountHead(head))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AccountHeadItem(head: AccountHead, onClick: (AccountHead) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(head) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = head.accountHeadName,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = head.accountGroup?.classification?.name ?: ""
            )
        }
    }
}
@file:OptIn(ExperimentalTime::class)

package com.diffy.broke.presentation.transaction

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.diffy.broke.R
import com.diffy.broke.domain.model.Transaction
import com.diffy.broke.domain.model.TransactionFormModes
import com.diffy.broke.presentation.core.slidingdrawer.SlidingDrawerState
import com.diffy.broke.presentation.core.templates.OnShowDialog
import com.diffy.broke.presentation.core.components.MainAppbarExtraContent
import com.diffy.broke.presentation.transaction.components.AddEditDialog
import com.diffy.broke.presentation.transaction.components.TransactionCard
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    state: TransactionStates,
    onEvent: (TransactionEvents) -> Unit,
    drawerClick: (SlidingDrawerState) -> Unit,
    drawerState: SlidingDrawerState,
    onSelectClick: () -> Unit
) {

    state.selectedTransaction.OnShowDialog{
        if (it.mode != TransactionFormModes.DELETE) AddEditDialog(onEvent = onEvent, onSelectClick = onSelectClick, selectedTransaction = it)
        else {
            AlertDialog(
                onDismissRequest = { onEvent(TransactionEvents.SetSelectedTransaction(null)) },
                icon = { Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Icon")},
                title = { Text(text = "Delete Transaction") },
                text = { Text(text = "Are you sure you want to delete this transaction?") },
                confirmButton = {
                    Button(
                        onClick = { onEvent(TransactionEvents.DeleteTransaction(it))}
                    ) {
                        Text(text = "Yes, Delete")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { onEvent(TransactionEvents.SetSelectedTransaction(null)) },
                    ) {
                        Text("No, Cancel")
                    }
                }
            )
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    val emptyTransaction = Transaction.new()
                    onEvent(TransactionEvents.SetSelectedTransaction(emptyTransaction))
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.fab)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = stringResource(R.string.transaction))
                }
            )
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.transaction)) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            drawerClick(drawerState.opposite())
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(R.string.navigation_icon)
                        )
                    }
                }
            )
        }
    ) { padding ->
        MainAppbarExtraContent(modifier = Modifier.padding(padding),state, onEvent)
        if(state.loadingState || state.transactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = if(state.transactions.isEmpty()) stringResource(R.string.no_transactions) else state.transactionMsg, style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(
                    items = state.transactions,
                    key = { it.id }
                ) {
                    var isMenuVisible by remember { mutableStateOf(false) }
                    TransactionCard(
                        transaction = it,
                        onClick = null,
                        onLongClick = { isMenuVisible = !isMenuVisible }
                    ) { pressOffset, itemHeight, itemWidth ->
                        DropdownMenu(
                            expanded = isMenuVisible,
                            onDismissRequest = { isMenuVisible = !isMenuVisible },
                            offset = pressOffset.copy(y = pressOffset.y - itemHeight,x = pressOffset.x - itemWidth ),
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    onEvent(TransactionEvents.SetSelectedTransaction(it.copy(mode = TransactionFormModes.EDIT)))
                                    isMenuVisible = !isMenuVisible
                                    onEvent(TransactionEvents.ShowAddEditDialog)
                                },
                                text = { Text(text = stringResource(R.string.edit) ) }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    onEvent(TransactionEvents.SetSelectedTransaction(it.copy(mode = TransactionFormModes.DELETE)))
                                    isMenuVisible = !isMenuVisible
                                },
                                text = { Text(text = stringResource(R.string.delete) ) }
                            )
                        }
                    }
                }

                item(
                    key = "spacer"
                ) {
                    Spacer(modifier = Modifier
                        .height(padding.calculateTopPadding()))
                }
            }
        }
    }
}
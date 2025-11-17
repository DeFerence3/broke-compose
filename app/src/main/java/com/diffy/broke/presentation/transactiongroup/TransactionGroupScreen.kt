package com.diffy.broke.presentation.transactiongroup

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.diffy.broke.R
import com.diffy.broke.presentation.core.slidingdrawer.SlidingDrawerState
import com.diffy.broke.presentation.core.templates.OnShowDialog
import com.diffy.broke.presentation.core.templates.ScaffoldTemplate
import com.diffy.broke.presentation.core.components.BrokeDialog
import com.diffy.broke.presentation.core.components.ClickableTextField
import com.diffy.broke.presentation.core.util.ObserveEvent
import kotlinx.coroutines.flow.Flow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionGroupScreen(
    onEvent: (TransactionGroupAction) -> Unit,
    state: TransactionGroupState,
    oneTimeEventChannelFlow: Flow<TransactionGroupEvent>,
    drawerState: SlidingDrawerState,
    drawerClick: (SlidingDrawerState) -> Unit,
) {
    val context = LocalContext.current


    oneTimeEventChannelFlow.ObserveEvent {
        when (it) {
            is TransactionGroupEvent.Success -> {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
            is TransactionGroupEvent.Error -> {
                Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Dialog for adding or editing an account group
    state.selectedTransactionGroup.OnShowDialog {
        BrokeDialog(
            title = if (state.selectedTransactionGroup?.id == 0) "Add Transaction Group" else "Edit Transaction Group",
            onNegativeAction = { onEvent(TransactionGroupAction.AddTransactionGroup) },
            onPositiveAction = { onEvent(TransactionGroupAction.SaveTransactionHead) },
            negativeText = "Cancel",
            positiveText = "Submit",
            positiveButtonEnabled = !state.selectedTransactionGroup?.name.isNullOrBlank()
        ) {
            OutlinedTextField(
                value = state.selectedTransactionGroup?.name ?: "",
                onValueChange = { newValue ->

                },
                label = { Text("Transaction Group Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            ClickableTextField(
                value = state.selectedTransactionGroup?.name ?: "",
                onClick = { },
                label = "Classification"
            )

        }
    }

    ScaffoldTemplate(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.transaction_group)) },
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
            onEvent(TransactionGroupAction.AddTransactionGroup)
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
                Text("Loading account groups...")
            } else if (state.transactionGroups.isEmpty()) {
                Text("No account groups found. Tap 'Add' to create one.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {

                }
            }
        }
    }
}

/*
@Composable
fun TransactionGroupItem(group: TransactionGroup, onClick: (TransactionGroup) -> Unit) {
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(group) },
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = androidx.compose.material3.MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = group.transactionGroupName,
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = group.classification.name
            )
        }
    }
}*/

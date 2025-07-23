package com.diffy.broke.presentation.accountgroup

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.diffy.broke.data.entity.Classification
import com.diffy.broke.domain.model.AccountGroup
import com.diffy.broke.presentation.core.search.SearchContract
import com.diffy.broke.presentation.core.slidingdrawer.SlidingDrawerState
import com.diffy.broke.presentation.core.templates.OnShowDialog
import com.diffy.broke.presentation.core.templates.ScaffoldTemplate
import com.diffy.broke.presentation.core.ui.components.BrokeDialog
import com.diffy.broke.presentation.core.ui.components.ClickableTextField
import com.diffy.broke.presentation.core.ui.util.ObserveEvent
import kotlinx.coroutines.flow.Flow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountGroupScreen(
    onEvent: (AccountGroupEvent) -> Unit,
    state: AccountGroupState,
    oneTimeEventChannelFlow: Flow<AccountGroupOneTimeEvent>,
    drawerState: SlidingDrawerState,
    drawerClick: (SlidingDrawerState) -> Unit,
) {
    val context = LocalContext.current

    val classification = rememberLauncherForActivityResult(
        contract = SearchContract(Classification::class),
        onResult = { item ->
            item?.let { newValue ->
                val updatedAccountGroup = state.selectedAccountGroup?.copy(classification = newValue) ?: AccountGroup.new()
                onEvent(AccountGroupEvent.SelectAccountGroup(updatedAccountGroup))
            }
        })

    oneTimeEventChannelFlow.ObserveEvent {
        when (it) {
            is AccountGroupOneTimeEvent.Success -> {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
            is AccountGroupOneTimeEvent.Error -> {
                Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Dialog for adding or editing an account group
    (state.selectedAccountGroup != null).OnShowDialog {
        BrokeDialog(
            title = if (state.selectedAccountGroup?.id == 0) "Add Account Group" else "Edit Account Group",
            onNegativeAction = { onEvent(AccountGroupEvent.HideAddOrEditDialog) },
            onPositiveAction = { onEvent(AccountGroupEvent.SaveAccountHead) },
            negativeText = "Cancel",
            positiveText = "Submit",
            positiveButtonEnabled = !state.selectedAccountGroup?.accountGroupName.isNullOrBlank()
        ) {
            OutlinedTextField(
                value = state.selectedAccountGroup?.accountGroupName ?: "",
                onValueChange = { newValue ->
                    val updatedAccountGroup = state.selectedAccountGroup?.copy(accountGroupName = newValue)
                        ?: AccountGroup.new()
                    onEvent(AccountGroupEvent.SelectAccountGroup(updatedAccountGroup))
                },
                label = { Text("Account Group Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            ClickableTextField(
                value = state.selectedAccountGroup?.classification?.name ?: "",
                onClick = { classification.launch(Unit)},
                label = "Classification"
            )

        }
    }

    ScaffoldTemplate(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.account_group)) },
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
            onEvent(AccountGroupEvent.AddAccountGroup)
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
            } else if (state.accountGroups.isEmpty()) {
                Text("No account groups found. Tap 'Add' to create one.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(state.accountGroups, key = { it.id }) { group ->
                        AccountGroupItem(group = group) {
                            onEvent(AccountGroupEvent.SelectAccountGroup(group))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AccountGroupItem(group: AccountGroup, onClick: (AccountGroup) -> Unit) {
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(group) }, // Made card clickable
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = androidx.compose.material3.MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = group.accountGroupName,
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
package com.diffy.broke.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.diffy.broke.state.Events
import com.diffy.broke.state.States
import com.diffy.broke.ViewModel
import com.diffy.broke.components.AddEditPackDialog
import com.diffy.broke.components.CustomAppBar
import com.diffy.broke.components.MainAppbarActions
import com.diffy.broke.components.MainAppbarExtraContent
import com.diffy.broke.components.TransactionItem
import com.diffy.broke.components.TransactionsHeader
import com.diffy.broke.utilcomponents.formatDateFromMilliseconds
import kotlin.collections.forEach as forEach

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    state: States,
    onEvent: (Events) -> Unit,
    navController: NavHostController,
    viewmodel: ViewModel
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomAppBar(
                "Broke",
                scrollBehavior,
                { MainAppbarActions( navController ) }
            ) {
                MainAppbarExtraContent(onEvent = onEvent, navController = navController,state)
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    onEvent(Events.ShowAddDialog)
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add new transaction"
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Transaction")
                }
            )
        },
    ) { padding ->

        if (state.isCreatingTransaction || state.isEditingTransaction) {
            AddEditPackDialog(state = state, onEvent = onEvent,viewModel = viewmodel)
        }

        if(state.transactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No transactions", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentPadding = padding,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                state.transactions.forEach { transactionsInTimeperiod ->
                    stickyHeader {
                        TransactionsHeader(formatDateFromMilliseconds(transactionsInTimeperiod.day))
                    }
                    items(transactionsInTimeperiod.rangedTransactions) { transaction ->
                        TransactionItem(
                            transactionwithtags = transaction,
                            onEvent = onEvent
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(padding.calculateTopPadding()))
                }
            }
        }
    }
}
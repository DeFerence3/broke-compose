package com.diffy.broke.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.diffy.broke.Events
import com.diffy.broke.States
import com.diffy.broke.components.AddPackDialog
import com.diffy.broke.components.CustomAppBar
import com.diffy.broke.components.TransactionItem
import com.diffy.broke.database.Databases
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    state: States,
    onEvent: (Events) -> Unit,
    navController: NavHostController
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { CustomAppBar(scrollBehavior, onEvent, navController) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    onEvent(Events.ShowDialog)
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

        if (state.isCreatingTransaction) {
            AddPackDialog(state = state, onEvent = onEvent)
        }

        if(state.transactions.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
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
                items(state.transactions) { transaction ->
                    TransactionItem(
                        transaction = transaction,
                        onEvent = onEvent
                    )
                }
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
fun formatDateFromMilliseconds(milliseconds: Long): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy - EEE")
    val date = Date(milliseconds)
    return dateFormat.format(date)
}
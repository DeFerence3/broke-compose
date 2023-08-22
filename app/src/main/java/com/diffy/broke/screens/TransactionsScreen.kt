package com.diffy.broke.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.diffy.broke.Events
import com.diffy.broke.States
import com.diffy.broke.components.AddPackDialog
import com.diffy.broke.components.CustomAppBar
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.reflect.KFunction1


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    state: States,
    onEvent: (Events) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val density = LocalDensity.current
    var itemHeight by remember { mutableStateOf(0.dp) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .onSizeChanged {
             itemHeight = with(density) { it.height.toDp() }
        },
        topBar = { CustomAppBar(scrollBehavior, onEvent) },
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
                    Spacer(modifier = Modifier
                        .width(10.dp))
                    Text(text = "Transaction")
                }
            )
        },
    ) { padding ->

        var isLongPressing by remember { mutableStateOf(false) }
        var pressOffset by remember { mutableStateOf(DpOffset.Zero) }

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

                    DropdownMenu(
                        expanded = isLongPressing,
                        onDismissRequest = { isLongPressing = !isLongPressing },
                        offset = pressOffset.copy(
                            pressOffset.y - itemHeight
                        )
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                onEvent(Events.DeleteTransaction(transaction))
                            },
                            text = { Text(text = "Delete") }
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {

                            }
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        isLongPressing = !isLongPressing
                                        pressOffset = DpOffset(it.x.toDp(),it.y.toDp())
                                    },
                                )
                            }
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(16.dp)
                            ),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = if (transaction.isExp) {
                                    Icons.Default.RemoveCircle
                                } else {
                                    Icons.Default.AddCircle
                                },
                                contentDescription = "Expense or income",
                                modifier = Modifier.padding(16.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = transaction.transTitle,
                                    modifier = Modifier.padding(8.dp, 8.dp, 8.dp, 4.dp),
                                    style = MaterialTheme.typography.titleLarge,
                                )
                                Text(
                                    text = formatDateFromMilliseconds(transaction.day),
                                    modifier = Modifier.padding(8.dp, 4.dp, 8.dp, 8.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                            Text(
                                text = "₹" + transaction.transAmnt.toString(),
                                modifier = Modifier.padding(8.dp, 4.dp, 8.dp, 8.dp),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    }
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


@Composable
fun Navigations(
    navController: NavHostController,
    state: States,
    onEvent: KFunction1<Events, Unit>,
) {
    NavHost(navController = navController, startDestination = "start") {
        composable("start") {
            TransactionsScreen(state = state, onEvent = onEvent)
        }
        composable("next/{id}") {

        }
        composable("components") {

        }
    }
}
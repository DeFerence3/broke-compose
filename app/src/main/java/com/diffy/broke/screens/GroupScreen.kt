package com.diffy.broke.screens

import AnathorScreen
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.diffy.broke.GroupEvent
import com.diffy.broke.GroupState
import com.diffy.broke.components.AddPackDialog
import com.diffy.broke.components.CustomAppBar
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.reflect.KFunction1


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackScreen(
    state: GroupState,
    onEvent: (GroupEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { CustomAppBar(scrollBehavior, state, onEvent) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    onEvent(GroupEvent.ShowDialog)
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

        if (state.isCreatingPack) {
            AddPackDialog(state = state, onEvent = onEvent)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.transactions) { transactions ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                        }
                        .pointerInput(Unit){
                            detectTapGestures(
                                onLongPress = { onEvent(GroupEvent.DeleteTransaction(transactions)) },
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
                            imageVector = if (transactions.isExp) {
                                Icons.Default.RemoveCircle
                            } else {
                                Icons.Default.AddCircle
                            },
                            contentDescription = "Expense or income",
                            modifier = Modifier.padding(16.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = transactions.transTitle,
                                modifier = Modifier.padding(8.dp, 8.dp, 8.dp, 4.dp),
                                style = MaterialTheme.typography.titleLarge,
                            )
                            Text(
                                text = formatDateFromMilliseconds(transactions.day),
                                modifier = Modifier.padding(8.dp, 4.dp, 8.dp, 8.dp),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                        Text(
                            text = "₹" + transactions.transAmnt.toString(),
                            modifier = Modifier.padding(8.dp, 4.dp, 8.dp, 8.dp),
                            style = MaterialTheme.typography.bodyLarge,
                        )
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
    state: GroupState,
    onEvent: KFunction1<GroupEvent, Unit>,
) {
    NavHost(navController = navController, startDestination = "start" ) {
        composable("start") {
            PackScreen(state = state, onEvent = onEvent)
        }
        composable("next/{id}") {
            val argument = it.arguments?.getString("id")
            AnathorScreen(state = state, onEvent = onEvent, argument = argument)
        }
        composable("components") {

        }
    }
}
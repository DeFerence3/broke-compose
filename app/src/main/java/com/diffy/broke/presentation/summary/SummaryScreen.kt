package com.diffy.broke.presentation.summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diffy.broke.R
import com.diffy.broke.domain.model.TransactionByTag
import com.diffy.broke.presentation.core.slidingdrawer.SlidingDrawerState
import com.diffy.broke.presentation.core.templates.ScaffoldTemplate
import com.diffy.broke.presentation.core.monthpicker.rememberMonthPickerState
import com.diffy.broke.presentation.core.theme.BrokeTheme
import com.diffy.broke.presentation.core.util.DateMonthPicker
import com.diffy.broke.presentation.core.util.getStartOfMonthInMillis
import com.diffy.broke.presentation.core.util.scaffoldContent
import com.diffy.broke.presentation.summary.components.SummaryCard
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(drawerClick: (SlidingDrawerState) -> Unit, drawerState: SlidingDrawerState) {
    TopAppBarDefaults.enterAlwaysScrollBehavior()
    var isMonth by remember { mutableStateOf(true) }
    val calendar = Calendar.getInstance()
    var isDateRangePickerShowing by remember { mutableStateOf(false) }
    var incomeByTags by remember { mutableStateOf<List<TransactionByTag>>(emptyList()) }
    var expenseByTags by remember { mutableStateOf<List<TransactionByTag>>(emptyList()) }
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = getStartOfMonthInMillis(),
        initialSelectedEndDateMillis = System.currentTimeMillis()
    )
    val monthPickerState = rememberMonthPickerState(
        initialSelectedMonth = calendar.get(Calendar.MONTH),
        initialSelectedYear = calendar.get(Calendar.YEAR)
    )
    val startEndDates by remember { mutableStateOf(
        Pair(dateRangePickerState.selectedStartDateMillis!!,dateRangePickerState.selectedEndDateMillis!!)
    ) }
    if (isDateRangePickerShowing) {
        DateMonthPicker(
            onSetClicked = { start,end ->
                isMonth = true
            },
            onDismissRequest = { isDateRangePickerShowing = !isDateRangePickerShowing }
        )
    }

/*    LaunchedEffect(state) {
        viewmodel.getTotalSpendThisMonth()
            .collect { newSummary ->
                summaryData = newSummary
            }
    }

    LaunchedEffect(state) {
        viewmodel.getExpenseByTag()
            .collect{
                expenseByTags = it
            }
    }
    LaunchedEffect(state) {
        viewmodel.getIncomeByTag()
            .collect{
                incomeByTags = it
            }
    }*/

    ScaffoldTemplate(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.summary)) },
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
        LazyColumn(
            modifier = Modifier
                .scaffoldContent(padding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(6) { count ->
                SummaryCard(
                    title = if (count % 2 == 0) "Bank" else "Cash",
                    balance = "udsv",
                    expense = "jdvhj",
                    income = "jhdhjj"
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SummaryScreenPrev() {
    BrokeTheme {
        SummaryScreen({}, SlidingDrawerState.Closed)
    }
}
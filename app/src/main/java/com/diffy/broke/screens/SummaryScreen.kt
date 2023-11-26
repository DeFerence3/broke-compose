package com.diffy.broke.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.diffy.broke.state.Events
import com.diffy.broke.state.States
import com.diffy.broke.ViewModel
import com.diffy.broke.components.CustomAppBar
import com.diffy.broke.components.Overview
import com.diffy.broke.ui.TableCell
import com.diffy.broke.dataclasses.SummaryData
import com.diffy.broke.dataclasses.TransactionByTag
import com.diffy.broke.utilcomponents.MonthPickerCompo
import com.diffy.broke.utilcomponents.dateRangeFormatter
import com.diffy.broke.utilcomponents.getStartOfMonthInMillis
import com.diffy.broke.utilcomponents.monthAndDateFormatter
import com.diffy.broke.ui.monthpicker.rememberMonthPickerState
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(viewmodel: ViewModel, state: States, onEvent: (Events) -> Unit) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var isMonth by remember { mutableStateOf(true) }
    val calendar = Calendar.getInstance()
    var isDateRangePickerShowing by remember { mutableStateOf(false) }
    var summaryData by remember { mutableStateOf(SummaryData()) }
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
    val column1Weight = .4f
    val column2Weight = .6f
    if (isDateRangePickerShowing) {
        MonthPickerCompo(
            monthPickerState = monthPickerState,
            dateRangePickerState = dateRangePickerState,
            dateRangePicked = {
                isMonth = false
                onEvent(Events.SetStartDateInMillis(dateRangePickerState.selectedStartDateMillis!!))
                onEvent(Events.SetEndDateInMillis(dateRangePickerState.selectedEndDateMillis!!))
            },
            monthPicked = {
                isMonth = true
                onEvent(Events.SetStartDateInMillis(monthPickerState.selectedMonthStartInMillis))
                onEvent(Events.SetEndDateInMillis(monthPickerState.selectedMonthEndInMillis))

            },
            cancelClicked = { isDateRangePickerShowing = !isDateRangePickerShowing }
        )
    }

    LaunchedEffect(state) {
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
    }

    Scaffold(
        topBar= { CustomAppBar("Summary", scrollBehavior, null, null) },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize(),
            contentPadding = paddingValues
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .clickable {
                            isDateRangePickerShowing = !isDateRangePickerShowing
                        }
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.small
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isMonth) {
                            monthAndDateFormatter(monthPickerState.selectedMonth,monthPickerState.selectedYear)
                        }else {
                            dateRangeFormatter(startEndDates.first,startEndDates.second)
                        },
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp))
                Overview(
                    column1Weight = column1Weight,
                    column2Weight = column2Weight,
                    totalSpend = summaryData.totalSpend,
                    totalEarn = summaryData.totalEarn,
                    totalSaving = summaryData.savings
                )
            }
            item{
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp))
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Spends",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            if (expenseByTags.isEmpty()) {
                item {
                    Row (modifier = Modifier.fillMaxWidth()){
                        TableCell(text = "( No data )", weight = column2Weight)
                    }
                }
            } else {
                item{(expenseByTags).forEach { expenseByTag ->
                    Row (modifier = Modifier.fillMaxWidth()){
                        TableCell(text = expenseByTag.tag, weight = column1Weight)
                        TableCell(text = expenseByTag.totalAmount.toString(), weight = column2Weight)
                    }}
                }
            }
            item{
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp))
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Incomes",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            if (incomeByTags.isEmpty()) {
                item {
                    Row (modifier = Modifier.fillMaxWidth()){
                        TableCell(text = "( No data )", weight = column2Weight)
                    }
                }
            } else {
                item{incomeByTags.forEach  { incomeByTag ->
                    Row (modifier = Modifier.fillMaxWidth()){
                        TableCell(text = incomeByTag.tag, weight = column1Weight)
                        TableCell(text = incomeByTag.totalAmount.toString(), weight = column2Weight)
                    }
                }}
            }
        }
    }
}
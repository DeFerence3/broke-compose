package com.diffy.broke.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.diffy.broke.state.DateRange.*
import com.diffy.broke.state.Events
import com.diffy.broke.state.OrderBy
import com.diffy.broke.state.SortView.*
import com.diffy.broke.state.States
import com.diffy.broke.dataclasses.DateRangeItems
import com.diffy.broke.ui.monthpicker.rememberMonthPickerState
import com.diffy.broke.utilcomponents.MonthPickerCompo
import com.diffy.broke.utilcomponents.getStartOfMonthInMillis
import com.diffy.broke.utilcomponents.getStartOfWeekInMillis
import com.diffy.broke.utilcomponents.getTodayStartInMillis
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppbarExtraContent(
    onEvent: (Events) -> Unit,
    navController: NavController,
    state: States
) {

    val itemWidth = 20.dp
    val calendar = Calendar.getInstance()
    var isDateRangePickerShowing by remember { mutableStateOf(false) }
    var viewSelectionMenu by remember { mutableStateOf(false) }
    var selectedSortView by remember { mutableStateOf("") }
    var orderBy by remember { mutableStateOf("") }
    var selectedRangeView by remember { mutableStateOf("ThisMonth") }
    var selectedRangeViewMenuOpen by remember { mutableStateOf(false) }
    val pressOffset by remember { mutableStateOf(DpOffset.Zero) }
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = getStartOfMonthInMillis(),
        initialSelectedEndDateMillis = System.currentTimeMillis()
    )
    val monthPickerState = rememberMonthPickerState(
        initialSelectedMonth = calendar.get(Calendar.MONTH),
        initialSelectedYear = calendar.get(Calendar.YEAR)
    )
    val dateRangeItems = listOf(
        DateRangeItems("Today") { onEvent(Events.DateRangeBy(TODAY)) },
        DateRangeItems("ThisWeek") { onEvent(Events.DateRangeBy(WEEK)) },
        DateRangeItems("ThisMonth") { onEvent(Events.DateRangeBy(MONTH)) },
        DateRangeItems("Custom") { onEvent(Events.DateRangeBy(CUSTOM)) },
    )

    if (isDateRangePickerShowing) {
        MonthPickerCompo(
            monthPickerState = monthPickerState,
            dateRangePickerState = dateRangePickerState,
            dateRangePicked = {
                onEvent(Events.SetStartDateInMillis(dateRangePickerState.selectedStartDateMillis!!))
                onEvent(Events.SetEndDateInMillis(dateRangePickerState.selectedEndDateMillis!!))
            },
            monthPicked = {
                onEvent(Events.SetStartDateInMillis(monthPickerState.selectedMonthStartInMillis))
                onEvent(Events.SetEndDateInMillis(monthPickerState.selectedMonthEndInMillis))
            },
            cancelClicked = { isDateRangePickerShowing = !isDateRangePickerShowing }
        )
    }

    selectedRangeView = when(state.transactionsDateRange) {

        TODAY -> "Today".also {
            onEvent(Events.SetStartDateInMillis(getTodayStartInMillis()))
            onEvent(Events.SetEndDateInMillis(System.currentTimeMillis()))
        }
        WEEK -> "ThisWeek".also {
            onEvent(Events.SetStartDateInMillis(getStartOfWeekInMillis()))
            onEvent(Events.SetEndDateInMillis(System.currentTimeMillis()))
        }
        MONTH -> "ThisMonth".also {
            onEvent(Events.SetStartDateInMillis(getStartOfMonthInMillis()))
            onEvent(Events.SetEndDateInMillis(System.currentTimeMillis()))
        }
        CUSTOM -> "Custom"
    }

    selectedSortView = when (state.sortView) {
        INCOME -> "Income"
        EXPENSE -> "Expense"
        ALL -> "All"
    }

    orderBy =
        if (state.transactionsOrderBy == OrderBy.ASCENDING) {
            "ASC"
        } else {
            "DESC"
        }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        //View selection chip
        InputChip(
            selected = viewSelectionMenu,
            onClick = { viewSelectionMenu = !viewSelectionMenu },
            label = { Text(text = selectedSortView) },
            leadingIcon = {
                Icon(
                    imageVector = if (viewSelectionMenu) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = "All/Expense/Income",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        )

        //Range selection chip
        InputChip(
            selected = selectedRangeViewMenuOpen,
            onClick = {
                selectedRangeViewMenuOpen = !selectedRangeViewMenuOpen
            },
            label = { Text(text = selectedRangeView) },
            leadingIcon = {
                Icon(
                    imageVector = if (selectedRangeViewMenuOpen) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = "view range",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            },
        )

        //Order toggle chip
        FilterChip(
            selected = false,
            onClick = { if (state.transactionsOrderBy == OrderBy.ASCENDING) {
                onEvent(Events.OrderPacks(OrderBy.DESCENDING))
            } else {
                onEvent(Events.OrderPacks(OrderBy.ASCENDING))
            } },
            label = { Text(text = orderBy) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Sort,
                    contentDescription = "Sort by ascending or descending",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        )

        //View selection dropdown
        DropdownMenu(
            expanded = viewSelectionMenu,
            onDismissRequest = { viewSelectionMenu = !viewSelectionMenu },
            modifier = Modifier
                .wrapContentSize()
        ) {
            DropdownMenuItem(
                onClick = {
                    onEvent(Events.SortViewBy(ALL))
                    viewSelectionMenu = false
                },
                text = { Text(text = "All") }
            )
            DropdownMenuItem(
                onClick = {
                    onEvent(Events.SortViewBy(INCOME))
                    viewSelectionMenu = false
                },
                text = { Text(text = "Income") }
            )
            DropdownMenuItem(
                onClick = {
                    onEvent(Events.SortViewBy(EXPENSE))
                    viewSelectionMenu = false
                },
                text = { Text(text = "Expense") }
            )
        }

        //Range selection dropdown
        DropdownMenu(
            expanded = selectedRangeViewMenuOpen,
            onDismissRequest = { selectedRangeViewMenuOpen = !selectedRangeViewMenuOpen },
            offset = pressOffset.copy(
                x = (pressOffset.x + itemWidth),
            ),
            modifier = Modifier
                .wrapContentSize()
        ) {
            dateRangeItems.forEach {
                DropdownMenuItem(
                    onClick = {
                        it.onClick()
                        selectedRangeViewMenuOpen = false
                        selectedRangeView = it.dateRange
                        if (it.dateRange == "Custom") isDateRangePickerShowing = !isDateRangePickerShowing
                    },
                    text = { Text(text = it.dateRange) }
                )
            }
        }

        IconButton(onClick = { navController.navigate("summary") }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                contentDescription = "report"
            )
        }
    }
}
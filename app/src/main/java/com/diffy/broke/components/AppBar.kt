package com.diffy.broke.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberDateRangePickerState
import com.diffy.broke.GroupEvent
import com.diffy.broke.GroupState
import com.diffy.broke.OrderBy
import com.diffy.broke.SortView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    state: GroupState,
    onEvent: (GroupEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        var viewAbout by remember { mutableStateOf(false) }
        val dateRangeState = rememberDateRangePickerState()
        if (state.isSelectingDateRange) {
            dateRangePicker(state = state, dateRangeState = dateRangeState, onEvent = onEvent)
        }

        if (viewAbout) AboutDialog(onDismiss = { viewAbout = !viewAbout })

        LargeTopAppBar(
            title = {
                Text(
                    text = "Broke",
                )
            },
            scrollBehavior = scrollBehavior,
            actions = {
                IconButton(
                    onClick = {
                        viewAbout = !viewAbout
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info"
                    )
                }
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            var viewSelected by remember { mutableStateOf(false) }
            var dateRange by remember { mutableStateOf(false) }
            var selectedSortView by remember { mutableStateOf("All") }
            var selectedDateRange by remember { mutableStateOf("All") }
            var orderBy by remember { mutableStateOf(true) }

            when (selectedSortView) {
                "All" -> onEvent(GroupEvent.SortViewBy(SortView.ALL))
                "Income" -> onEvent(GroupEvent.SortViewBy(SortView.INCOME))
                "Expense" -> onEvent(GroupEvent.SortViewBy(SortView.EXPENSE))
            }


            if (orderBy) {
                onEvent(GroupEvent.OrderPacks(OrderBy.ASENDING))
            } else {
                onEvent(GroupEvent.OrderPacks(OrderBy.DECENDING))
            }


            FilterChip(
                selected = viewSelected,
                onClick = { viewSelected = !viewSelected },
                label = { Text(text = selectedSortView) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Localized Description",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            )

            /*FilterChip(
                selected = false,
                onClick = { dateRange = !dateRange },
                label = { Text(text = selectedDateRange) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "Date Range",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            )*/

            FilterChip(
                selected = false,
                onClick = {
                    orderBy = !orderBy
                },
                label = { Text(text = if (orderBy) "ASC" else "DESC") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Sort,
                        contentDescription = "Sort by ascending or descending",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            )

            DropdownMenu(
                expanded = viewSelected,
                onDismissRequest = { viewSelected = false },
                modifier = Modifier
                    .wrapContentSize()
            ) {
                DropdownMenuItem(
                    onClick = {
                        selectedSortView = "All"
                        viewSelected = false
                    },
                    text = { Text(text = "All") }
                )
                DropdownMenuItem(
                    onClick = {
                        selectedSortView = "Income"
                        viewSelected = false
                    },
                    text = { Text(text = "Income") }
                )
                DropdownMenuItem(
                    onClick = {
                        selectedSortView = "Expense"
                        viewSelected = false
                    },
                    text = { Text(text = "Expense") }
                )
            }

            DropdownMenu(
                expanded = dateRange,
                onDismissRequest = { dateRange = false },
                modifier = Modifier
                    .wrapContentSize()
            ) {

                DropdownMenuItem(
                    onClick = {
                        selectedDateRange = "Custom"
                        dateRange = false
                        onEvent(GroupEvent.ShowDateRangePicker)
                    },
                    text = { Text(text = "Custom") }
                )
                DropdownMenuItem(
                    onClick = {
                        selectedDateRange = "Week"
                        dateRange = false
                    },
                    text = { Text(text = "Week") }
                )
                DropdownMenuItem(
                    onClick = {
                        selectedDateRange = "Month"
                        dateRange = false
                    },
                    text = { Text(text = "Month") }
                )
                DropdownMenuItem(
                    onClick = {
                        selectedDateRange = "Year"
                        dateRange = false
                    },
                    text = { Text(text = "Year") }
                )
            }
        }
    }
}

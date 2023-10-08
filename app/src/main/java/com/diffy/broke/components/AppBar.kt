package com.diffy.broke.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
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
import androidx.navigation.NavHostController
import com.diffy.broke.DateRange
import com.diffy.broke.Events
import com.diffy.broke.OrderBy
import com.diffy.broke.SortView
import com.diffy.broke.utilcomponents.DateRangePickerScreen
import com.diffy.broke.utilcomponents.getStartOfMonthInMillis
import com.diffy.broke.utilcomponents.getStartOfWeekInMillis
import com.diffy.broke.utilcomponents.getTodayStartInMillis

data class DateRangeItems(
    val dateRange: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onEvent: (Events) -> Unit,
    navController: NavHostController
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        var viewAbout by remember { mutableStateOf(false) }
        var isMenuExpanded by remember { mutableStateOf(false) }
        if (viewAbout) AboutDialog(onDismiss = { viewAbout = !viewAbout })

        LargeTopAppBar(
            title = {
                Text( text = "Broke")
            },
            scrollBehavior = scrollBehavior,
            actions = {
                IconButton(
                    onClick = { isMenuExpanded = !isMenuExpanded }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Options"
                    )
                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = { isMenuExpanded = !isMenuExpanded },
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text("Backup")
                            },
                            onClick = {
                                navController.navigate("backup-page")
                                isMenuExpanded = !isMenuExpanded
                            },
                        )
                        DropdownMenuItem(
                            text = {
                                Text("About")
                            },
                            onClick = {
                                viewAbout = !viewAbout
                                isMenuExpanded = !isMenuExpanded
                            },
                        )
                    }
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
            var viewSelectionMenu by remember { mutableStateOf(false) }
            var selectedSortView by remember { mutableStateOf("All") }
            var orderBy by remember { mutableStateOf(true) }
            var selectedRangeView by remember { mutableStateOf("AllDay") }
            var selectedRangeViewMenuOpen by remember { mutableStateOf(false) }
            val pressOffset by remember { mutableStateOf(DpOffset.Zero) }
            val dateRangePickerState = rememberDateRangePickerState()
            var dateRangePickerScreen by remember { mutableStateOf(false) }

            if (dateRangePickerScreen) {
                DateRangePickerScreen(
                    dateRangePickerState,
                    {dateRangePickerScreen = !dateRangePickerScreen},
                    {
                        onEvent(Events.SetStartDateInMillis(dateRangePickerState.selectedStartDateMillis!!))
                        onEvent(Events.SetEndDateInMillis(dateRangePickerState.selectedEndDateMillis!!))
                    }
                )
            }

            val itemWidth = 20.dp

            val dateRangeItems = listOf(
                DateRangeItems("AllDay"),
                DateRangeItems("Today"),
                DateRangeItems("ThisWeek"),
                DateRangeItems("ThisMonth"),
                DateRangeItems("Custom"),
            )

            when(selectedRangeView) {
                "AllDay" -> onEvent(Events.DateRangeBy(DateRange.ALLDAY))
                "Today" -> {
                    onEvent(Events.DateRangeBy(DateRange.RANGED))
                    onEvent(Events.SetStartDateInMillis(getTodayStartInMillis()))
                    onEvent(Events.SetEndDateInMillis(System.currentTimeMillis()))
                }
                "ThisWeek" -> {
                    onEvent(Events.DateRangeBy(DateRange.RANGED))
                    onEvent(Events.SetStartDateInMillis(getStartOfWeekInMillis()))
                    onEvent(Events.SetEndDateInMillis(System.currentTimeMillis()))
                }
                "ThisMonth" -> {
                    onEvent(Events.DateRangeBy(DateRange.RANGED))
                    onEvent(Events.SetStartDateInMillis(getStartOfMonthInMillis()))
                    onEvent(Events.SetEndDateInMillis(System.currentTimeMillis()))
                }
                "Custom" -> onEvent(Events.DateRangeBy(DateRange.RANGED))
            }
            when (selectedSortView) {
                "All" -> onEvent(Events.SortViewBy(SortView.ALL))
                "Income" -> onEvent(Events.SortViewBy(SortView.INCOME))
                "Expense" -> onEvent(Events.SortViewBy(SortView.EXPENSE))
            }

            if (orderBy) {
                onEvent(Events.OrderPacks(OrderBy.ASENDING))
            } else {
                onEvent(Events.OrderPacks(OrderBy.DECENDING))
            }

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
                onClick = { orderBy = !orderBy },
                label = { Text(text = if (orderBy) "ASC" else "DESC") },
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
                        selectedSortView = "All"
                        viewSelectionMenu = false
                    },
                    text = { Text(text = "All") }
                )
                DropdownMenuItem(
                    onClick = {
                        selectedSortView = "Income"
                        viewSelectionMenu = false
                    },
                    text = { Text(text = "Income") }
                )
                DropdownMenuItem(
                    onClick = {
                        selectedSortView = "Expense"
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
                            selectedRangeViewMenuOpen = false
                            selectedRangeView = it.dateRange
                            if (it.dateRange == "Custom") dateRangePickerScreen = !dateRangePickerScreen
                        },
                        text = { Text(text = it.dateRange) }
                    )
                }
            }
        }
    }
}

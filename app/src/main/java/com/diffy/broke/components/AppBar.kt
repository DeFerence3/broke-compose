package com.diffy.broke.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Sort
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
import com.diffy.broke.helpers.DateRangePickerScreen

data class DateRangeItems(
    val dateRange: String,
    var dateRangePickerScreen: Boolean,
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
            var selectedRangeView by remember { mutableStateOf("All Day") }
            var selectedRangeViewMenuOpen by remember { mutableStateOf(false) }
            val pressOffset by remember { mutableStateOf(DpOffset.Zero) }
            val dateRangePickerState = rememberDateRangePickerState()
            var dateRangePickerScreen by remember { mutableStateOf(false) }

            if (dateRangePickerScreen) {
                DateRangePickerScreen(dateRangePickerState,onDismiss = {dateRangePickerScreen = !dateRangePickerScreen})
            }

            Log.i("StartDate",dateRangePickerState.selectedStartDateMillis.toString())
            Log.i("EndDate",dateRangePickerState.selectedEndDateMillis.toString())

            val itemWidth = 20.dp

            val dateRangeItems = listOf(
                DateRangeItems("AllDay",dateRangePickerScreen),
                DateRangeItems("Today", dateRangePickerScreen),
                DateRangeItems("ThisWeek", dateRangePickerScreen),
                DateRangeItems("ThisMonth", dateRangePickerScreen),
                DateRangeItems("Custom", dateRangePickerScreen),
            )
            when (selectedSortView) {
                "All" -> onEvent(Events.SortViewBy(SortView.ALL))
                "Income" -> onEvent(Events.SortViewBy(SortView.INCOME))
                "Expense" -> onEvent(Events.SortViewBy(SortView.EXPENSE))
            }

            when (selectedRangeView) {
                "All Day" -> onEvent(Events.DateRangeBy(DateRange.ALLDAY))
                "Today" -> onEvent(Events.DateRangeBy(DateRange.ALLDAY))
                "ThisWeek" -> onEvent(Events.DateRangeBy(DateRange.ALLDAY))
                "ThisMonth" -> onEvent(Events.DateRangeBy(DateRange.ALLDAY))
                "Custom" -> onEvent(Events.DateRangeBy(DateRange.ALLDAY))
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
                        imageVector = Icons.Filled.Sort,
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
                    DropdownMenuItem(
                        onClick = {
                            selectedRangeView = "it.dateRange"
                            selectedRangeViewMenuOpen = false
                            dateRangePickerScreen = true
                        },
                        text = { Text(text = "it.dateRange") }
                    )
                dateRangeItems.forEach {
                }
            }
        }
    }
}
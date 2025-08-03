package com.diffy.broke.presentation.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.diffy.broke.domain.model.DateRangeItems
import com.diffy.broke.presentation.core.enums.ViewType
import com.diffy.broke.presentation.core.templates.OnShowDialog
import com.diffy.broke.presentation.core.ui.util.DateMonthPicker
import com.diffy.broke.presentation.transaction.TransactionEvents
import com.diffy.broke.presentation.transaction.TransactionStates

@Composable
fun MainAppbarExtraContent(
    transactionStates: TransactionStates,
    onEvent: (TransactionEvents) -> Unit
) {

    val itemWidth = 20.dp
    var selectedRangeViewMenuOpen by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val pressOffset by remember { mutableStateOf(DpOffset.Zero) }
    val dateRangeItems = listOf(
        DateRangeItems("Today") {
            onEvent(TransactionEvents.SetViewType(ViewType.Today))
        },
        DateRangeItems("ThisWeek") {
            onEvent(TransactionEvents.SetViewType(ViewType.ThisWeek))
        },
        DateRangeItems("ThisMonth") {
            onEvent(TransactionEvents.SetViewType(ViewType.ThisMonth))
        },
        DateRangeItems("Custom") {
            showDatePicker = showDatePicker.not()
        },
    )

    showDatePicker.OnShowDialog{
        DateMonthPicker(
            onSetClicked = { start,end ->
                showDatePicker = !showDatePicker
                onEvent(TransactionEvents.SetViewType(ViewType.Custom(start,end)))
            },
            onDismissRequest = {
                showDatePicker = !showDatePicker
            }
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        //View selection chip
        /*InputChip(
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
        )*/

        //Range selection chip
        InputChip(
            selected = selectedRangeViewMenuOpen,
            onClick = {
                selectedRangeViewMenuOpen = !selectedRangeViewMenuOpen
            },
            label = { Text(text = transactionStates.viewType.javaClass.simpleName) },
            leadingIcon = {
                Icon(
                    imageVector = if (selectedRangeViewMenuOpen) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = "view range",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            },
        )

        //Order toggle chip
        /*FilterChip(
            selected = false,
            onClick = {  },
            label = { Text(text = orderBy.name) },
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
                    viewSelectionMenu = false
                },
                text = { Text(text = "All") }
            )
            DropdownMenuItem(
                onClick = {
                    viewSelectionMenu = false
                },
                text = { Text(text = "Income") }
            )
            DropdownMenuItem(
                onClick = {
                    viewSelectionMenu = false
                },
                text = { Text(text = "Expense") }
            )
        }*/

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
                    },
                    text = { Text(text = it.dateRange) }
                )
            }
        }
    }
}
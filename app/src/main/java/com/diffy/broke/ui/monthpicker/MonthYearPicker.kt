package com.diffy.broke.ui.monthpicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar

class MonthPickerState(initialSelectedMonth: Int, initialSelectedYear: Int) {
    var selectedMonth by mutableIntStateOf(initialSelectedMonth)
    var selectedYear by mutableIntStateOf(initialSelectedYear)

    val selectedMonthStartInMillis: Long
        get() {
            val startDate = Calendar.getInstance().apply {
                set(Calendar.YEAR, selectedYear)
                set(Calendar.MONTH, selectedMonth)
                set(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            return startDate.timeInMillis
        }

    val selectedMonthEndInMillis: Long
        get() {
            val endDate = Calendar.getInstance().apply {
                set(Calendar.YEAR, selectedYear)
                set(Calendar.MONTH, selectedMonth + 1)
                set(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                add(Calendar.MILLISECOND, -1)
            }
            return endDate.timeInMillis
        }
}

@Composable
fun rememberMonthPickerState(initialSelectedMonth: Int, initialSelectedYear: Int): MonthPickerState {
    return rememberSaveable(
        saver = object : Saver<MonthPickerState, Pair<Int, Int>> {
            override fun restore(value: Pair<Int, Int>): MonthPickerState {
                return MonthPickerState(value.first, value.second)
            }

            override fun SaverScope.save(value: MonthPickerState): Pair<Int, Int> {
                return Pair(value.selectedMonth, value.selectedYear)
            }
        }
    ) {
        MonthPickerState(initialSelectedMonth, initialSelectedYear)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthPicker(
    monthPickerState: MonthPickerState
) {
    val months = listOf("JAN", "FEB", "MAR",
        "APR", "MAY", "JUN",
        "JUL", "AUG", "SEP",
        "OCT", "NOV", "DEC"
    )
    var selectedMonth by remember { mutableIntStateOf(monthPickerState.selectedMonth) }
    var selectedYear by remember { mutableIntStateOf(monthPickerState.selectedYear) }
    val interactionSource = remember { MutableInteractionSource() }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text(
                text = "Select Month",
                modifier = Modifier.padding(bottom = 20.dp),
                style = MaterialTheme.typography.titleLarge
            )
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(35.dp)
                        .rotate(90f)
                        .clickable(
                            indication = null,
                            interactionSource = interactionSource,
                            onClick = {
                                selectedYear--
                                monthPickerState.selectedYear = selectedYear
                            }
                        ),
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = selectedYear.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    modifier = Modifier
                        .size(35.dp)
                        .rotate(-90f)
                        .clickable(
                            indication = null,
                            interactionSource = interactionSource,
                            onClick = {
                                selectedYear++
                                monthPickerState.selectedYear = selectedYear
                            }
                        ),
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
        repeat(4) { rowIndex ->
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(3) { colIndex ->
                        val monthIndex = rowIndex * 3 + colIndex
                        if (monthIndex < months.size) {
                            item {
                                FilterChip(
                                    selected = selectedMonth == monthIndex,
                                    onClick = {
                                        selectedMonth = monthIndex
                                        monthPickerState.selectedMonth = monthIndex
                                    },
                                    label = { Text(text = months[monthIndex]) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
package com.diffy.broke.components

import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diffy.broke.GroupEvent
import com.diffy.broke.GroupState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dateRangePicker(
    state: GroupState,
    dateRangeState: DateRangePickerState,
    onEvent: (GroupEvent) -> Unit,
    modifier: Modifier = Modifier
) {
/*    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(GroupEvent.HideDateRangePicker)
        },
        title = { Text(text = "Pick A range") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DateRangePicker(state = dateRangeState, )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {  }
            ) {
                Text(text = "Set")
            }
        },
    )*/


}

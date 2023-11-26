package com.diffy.broke.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diffy.broke.ui.TableCell

@Composable
fun Overview(
    column1Weight: Float,
    column2Weight: Float,
    totalSpend: Float,
    totalEarn: Float,
    totalSaving: Float
) {
    Column {
        Text(
            modifier = Modifier.padding(8.dp),
            text = "Overview",
            style = MaterialTheme.typography.titleLarge
        )
        Row (modifier = Modifier.fillMaxWidth()){
            TableCell(text = "Total spending:", weight = column1Weight)
            TableCell(text = totalSpend.toString(), weight = column2Weight)
        }
        Row (modifier = Modifier.fillMaxWidth()){
            TableCell(text = "Total earning:", weight = column1Weight)
            TableCell(text = totalEarn.toString(), weight = column2Weight)
        }
        Row (modifier = Modifier.fillMaxWidth()){
            TableCell(text = "Total savings:", weight = column1Weight)
            TableCell(text = totalSaving.toString(), weight = column2Weight)
        }
    }
}
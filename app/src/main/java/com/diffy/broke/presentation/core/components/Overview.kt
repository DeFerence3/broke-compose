package com.diffy.broke.presentation.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Overview(
    column1Weight: Float,
    column2Weight: Float,
    totalSpend: Float,
    totalEarn: Float,
    totalSaving: Float
) {
    var isMenuVisible by remember { mutableStateOf(false) }

    Column (
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = "Overview",
            style = MaterialTheme.typography.titleLarge
        )
        /*TransactionCard(
            header = "TotalSpending",
            subHeader = null,
            icon = Icons.Default.MoneyOff,
            value = totalSpend.toString(),
            onClick = { null },
            onLongClick = { null }
        ) { _, _ -> null }
        TransactionCard(
            header = "Total Earning",
            subHeader = null,
            icon = Icons.Default.Wallet,
            value = totalEarn.toString(),
            onClick = { null },
            onLongClick = { null }
        ) { _, _ -> null }
        TransactionCard(
            header = "Total Savings",
            subHeader = null,
            icon = Icons.Default.Money,
            value = totalSaving.toString(),
            onClick = { println("Clicked") },
            onLongClick = { isMenuVisible = !isMenuVisible }
        ) { _, _ -> null }*/
    }
}

@Preview
@Composable
fun PreviewOverview() {
    Overview(1F,1F,1F,1F,1F)
}
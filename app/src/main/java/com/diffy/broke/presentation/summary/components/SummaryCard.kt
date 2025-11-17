package com.diffy.broke.presentation.summary.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.diffy.broke.presentation.core.theme.BrokeTheme

@Composable
fun SummaryCard(
    modifier: Modifier = Modifier,
    title: String,
    balance: String,
    expense: String,
    income: String
) {
    Card(
        modifier = modifier
            .height(IntrinsicSize.Min)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "₹ $balance",
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(35f, TextUnitType.Sp)
                )
            }
            HorizontalDivider()
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f),
                ) {
                    Text(
                        text = "Income On this month",
                        fontWeight = FontWeight.Bold
                    )
                    Text(income)
                }

                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Expense On this month",
                        fontWeight = FontWeight.Bold
                    )
                    Text(expense)
                }
            }
        }
    }
}

@Preview
@Composable
fun SummaryCardPrev() {
    BrokeTheme {
        SummaryCard(
            title = "Bank | SBI",
            balance = "2345.00",
            expense = "45.20",
            income = "4500.00"
        )
    }
}
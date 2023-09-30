package com.diffy.broke.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TransactionsHeader(transHeadTitle: String) {
    Text(
        text = transHeadTitle,
        modifier = Modifier.padding(8.dp, 8.dp, 8.dp, 4.dp),
        style = MaterialTheme.typography.titleLarge,
    )
}
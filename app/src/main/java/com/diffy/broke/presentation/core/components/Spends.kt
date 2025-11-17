package com.diffy.broke.presentation.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.diffy.broke.domain.model.TransactionByTag

@Composable
fun Spends(
    expenseWithTags: List<TransactionByTag>
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        expenseWithTags.forEach {
            /*TransactionCard(
                header = it.tag,
                subHeader = null,
                icon = Icons.Default.Money,
                value = it.totalAmount.toString(),
                onClick = { *//*TODO*//* },
                onLongClick = { *//*TODO*//* }
            ) { _, _ ->
                null
            }*/
        }
    }
}
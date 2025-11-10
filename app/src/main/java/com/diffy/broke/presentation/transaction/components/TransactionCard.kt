package com.diffy.broke.presentation.transaction.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.diffy.broke.domain.model.Transaction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun TransactionCard(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    onClick: (() -> Unit)?,
    onLongClick: () -> Unit,
    composable: @Composable (DpOffset, Dp, Dp) -> Unit?
) {
    val date = remember(transaction.day) {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(transaction.day))
    }
    var pressOffset by remember { mutableStateOf(DpOffset.Zero) }
    val density = LocalDensity.current
    var itemHeight by remember { mutableStateOf(0.dp) }
    var itemWidth by remember { mutableStateOf(0.dp) }
    val interactionSource = remember {
        MutableInteractionSource()
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .onSizeChanged {
                itemHeight = with(density) { it.height.toDp() }
                itemWidth = with(density) { it.width.toDp() }
            }
            .clickable(onClick != null) { onClick?.invoke() }
            .pointerInput(true) {
                detectTapGestures(
                    onLongPress = {
                        onLongClick()
                        pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                    },
                    onPress = {
                        val press = PressInteraction.Press(it)
                        interactionSource.emit(press)
                        tryAwaitRelease()
                        interactionSource.emit(PressInteraction.Release(press))
                    }
                )
            },
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 2.dp,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Category and Notes
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = transaction.category.categoryName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                if (transaction.notes.isNotBlank()) {
                    Text(
                        text = transaction.notes,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = date,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            val txtColor = if (transaction.isIncome)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.primary
            Text(
                modifier = Modifier
                    .padding(4.dp),
                text = "₹${transaction.amount}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = txtColor
            )
        }
        composable(pressOffset,itemHeight,itemWidth)
    }
}
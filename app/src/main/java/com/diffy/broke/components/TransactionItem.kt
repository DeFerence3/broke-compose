package com.diffy.broke.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.diffy.broke.Events
import com.diffy.broke.database.Transactions
import com.diffy.broke.screens.formatDateFromMilliseconds

@Composable
fun TransactionItem(
    transaction: Transactions,
    onEvent: (Events) -> Unit,
) {

    var isMenuVisible by rememberSaveable { mutableStateOf(false) }
    var pressOffset by remember { mutableStateOf(DpOffset.Zero) }
    val density = LocalDensity.current
    var itemHeight by remember { mutableStateOf(0.dp) }
    val interactionSource = remember {
        MutableInteractionSource()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged {
                itemHeight = with(density) { it.height.toDp() }
            }
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(16.dp)
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .indication(interactionSource, LocalIndication.current)
                .pointerInput(true) {
                    detectTapGestures(
                        onLongPress = {
                            isMenuVisible = !isMenuVisible
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (transaction.isExp) {
                    Icons.Default.RemoveCircle
                } else {
                    Icons.Default.AddCircle
                },
                contentDescription = "Expense or income",
                modifier = Modifier.padding(16.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.transTitle,
                    modifier = Modifier.padding(8.dp, 8.dp, 8.dp, 4.dp),
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = formatDateFromMilliseconds(transaction.day),
                    modifier = Modifier.padding(8.dp, 4.dp, 8.dp, 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Text(
                text = "₹" + transaction.transAmnt.toString(),
                modifier = Modifier.padding(8.dp, 4.dp, 8.dp, 8.dp),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        DropdownMenu(
            expanded = isMenuVisible,
            onDismissRequest = { isMenuVisible = !isMenuVisible },
            offset = pressOffset.copy(y = pressOffset.y - itemHeight ),
        ) {
            DropdownMenuItem(
                onClick = {
                    isMenuVisible = !isMenuVisible
                },
                text = { Text(text = "Edit" ) }
            )
            DropdownMenuItem(
                onClick = {
                    onEvent(Events.DeleteTransaction(transaction))
                    isMenuVisible = !isMenuVisible
                },
                text = { Text(text = "Delete" ) }
            )
        }
    }
}

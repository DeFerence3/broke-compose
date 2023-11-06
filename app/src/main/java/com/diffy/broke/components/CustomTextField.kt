package com.diffy.broke.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardactions: KeyboardActions,
    keyboardoptions: KeyboardOptions
) {

    val interactionSource = remember { MutableInteractionSource() }
    BasicTextField(
        modifier = modifier
            .height(30.dp)
            .width(IntrinsicSize.Min)
            .padding(3.dp),
        value = value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        ),
        interactionSource = interactionSource,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty() || value == "") {
                    Text(
                        text = "tag...",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            innerTextField.invoke()
        },
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        keyboardActions = keyboardactions,
        keyboardOptions = keyboardoptions,
    )
}
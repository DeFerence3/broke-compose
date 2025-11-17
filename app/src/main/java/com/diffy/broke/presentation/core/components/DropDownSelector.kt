package com.diffy.broke.presentation.core.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropDownSelector(
    expanded: Boolean,
    value: (T?) -> String?,
    onSearch: (String) -> Unit,
    onSelect: (T) -> Unit,
    onExpandedChange: (Boolean) -> Unit,
    listItems: List<T>,
    label: String? = null,
    selectedItem: T?
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ){
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable, true)
                .fillMaxWidth(),
            value = value(selectedItem) ?: "",
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            onValueChange = onSearch,
            label = label?.let { {
                Text(text = it)
            } }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            modifier = Modifier,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            listItems.map {
                DropdownMenuItem(
                    text = {
                        Text(value(it) ?: "")
                    },
                    onClick = {
                        onSelect(it)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }

}
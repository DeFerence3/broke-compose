package com.diffy.broke.presentation.core.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.diffy.broke.R

@Composable
fun MainAppbarActions(navigateToBackup: () -> Unit) {

    var isMenuExpanded by remember { mutableStateOf(false) }
    var viewAbout by remember { mutableStateOf(false) }
    if (viewAbout) AboutDialog(onDismiss = { viewAbout = !viewAbout })
    IconButton(
        onClick = { isMenuExpanded = !isMenuExpanded }
    ) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.more_options)
        )
        DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = !isMenuExpanded },
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(R.string.backup))
                },
                onClick = {
                    navigateToBackup()
                    isMenuExpanded = !isMenuExpanded
                },
            )
            DropdownMenuItem(
                text = {
                    Text(stringResource(R.string.about))
                },
                onClick = {
                    viewAbout = !viewAbout
                    isMenuExpanded = !isMenuExpanded
                },
            )
        }
    }
}
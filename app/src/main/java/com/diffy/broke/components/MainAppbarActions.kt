package com.diffy.broke.components

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
import androidx.navigation.NavController

@Composable
fun MainAppbarActions(
    navController: NavController
) {

    var isMenuExpanded by remember { mutableStateOf(false) }
    var viewAbout by remember { mutableStateOf(false) }
    if (viewAbout) AboutDialog(onDismiss = { viewAbout = !viewAbout })
    IconButton(
        onClick = { isMenuExpanded = !isMenuExpanded }
    ) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "More Options"
        )
        DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = !isMenuExpanded },
        ) {
            DropdownMenuItem(
                text = {
                    Text("Backup")
                },
                onClick = {
                    navController.navigate("backup-page")
                    isMenuExpanded = !isMenuExpanded
                },
            )
            DropdownMenuItem(
                text = {
                    Text("About")
                },
                onClick = {
                    viewAbout = !viewAbout
                    isMenuExpanded = !isMenuExpanded
                },
            )
        }
    }
}
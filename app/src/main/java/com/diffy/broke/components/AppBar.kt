package com.diffy.broke.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppBar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
    mainAppbarActions: @Composable (() -> Unit)?,
    extraContent: @Composable (() -> Unit)?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LargeTopAppBar(
            title = { Text( text = title) },
            scrollBehavior = scrollBehavior,
            actions = {
                mainAppbarActions?.invoke()
            }
        )
        extraContent?.invoke()
    }
}
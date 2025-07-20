package com.diffy.broke.presentation.config

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Config(
    state: ConfigState,
    onEvent: ConfigEvent,
    oneTimeEvent: ConfigOneTimeEvent
){
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "ConfigScreen")
    }
}
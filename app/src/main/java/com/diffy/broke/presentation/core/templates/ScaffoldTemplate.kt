package com.diffy.broke.presentation.core.templates

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun ScaffoldTemplate(
    topBar: @Composable () -> Unit = { },
    floatingActionButtonText: String? = null,
    floatingActionButtonAction: () -> Unit = { },
    content: @Composable (paddingValues: PaddingValues) -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            topBar()
        },
        floatingActionButton = {
            if (floatingActionButtonText != null) {
                ExtendedFloatingActionButton(
                    onClick = {
                        floatingActionButtonAction()
                    },
                    content = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "FAB"
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = floatingActionButtonText)
                    }
                )
            }
        }
    ) { paddingValues ->
        paddingValues
        content.invoke(paddingValues.plus(PaddingValues(8.dp)))
    }
}

operator fun PaddingValues.plus(other: PaddingValues): PaddingValues = PaddingValues(
    start = this.calculateStartPadding(LayoutDirection.Ltr) +
            other.calculateStartPadding(LayoutDirection.Ltr),
    top = this.calculateTopPadding() + other.calculateTopPadding(),
    end = this.calculateEndPadding(LayoutDirection.Ltr) +
            other.calculateEndPadding(LayoutDirection.Ltr),
    bottom = this.calculateBottomPadding() + other.calculateBottomPadding(),
)
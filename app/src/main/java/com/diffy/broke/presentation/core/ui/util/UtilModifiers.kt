package com.diffy.broke.presentation.core.ui.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun Modifier.scaffoldContent(scaffoldPadding: PaddingValues) : Modifier {
    return this
        .padding(scaffoldPadding)/*
        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))*/
        .padding(10.dp)
        .fillMaxSize()
}

fun Modifier.applyWhen(
    condition: Boolean,
    modifier: Modifier.() -> Modifier,
) : Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}

fun Modifier.multiTapTrigger(
    tapCount: Int = 5,
    timeoutMillis: Long = 1000L,
    onTrigger: () -> Unit,
): Modifier = composed {
    val clickCounter = remember { mutableIntStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    this.then(
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            clickCounter.intValue++

            coroutineScope.launch {
                delay(timeoutMillis)
                clickCounter.intValue = 0 // Reset after timeout
            }

            if (clickCounter.intValue >= tapCount) {
                onTrigger()
                clickCounter.intValue = 0 // Reset after triggering
            }
        }
    )
}

@Composable
fun <T> Flow<T>.ObserveEvent(onEvent: suspend (T) -> Unit) {
    val flow = this
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(flow, lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}
package com.diffy.broke.presentation.core.search

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.diffy.broke.presentation.core.Route
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

private const val RESULT_KEY = "result"

fun <T> NavController.navigateBackWithResult(value: T) {
    previousBackStackEntry?.savedStateHandle?.set(RESULT_KEY, value)
    navigateUp()
}

suspend fun <T> NavController.navigateForResult(route: Route): T? =
    suspendCancellableCoroutine { continuation ->
        val currentNavEntry = currentBackStackEntry
            ?: throw IllegalStateException("No current back stack entry found")
        navigate(route)

        val lifecycleObserver = object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_START) {
                    val result = currentNavEntry.savedStateHandle.get<T?>(RESULT_KEY)
                    if (result != null) {
                        continuation.resume(result)
                        currentNavEntry.savedStateHandle.remove<T>(RESULT_KEY)
                        currentNavEntry.lifecycle.removeObserver(this)
                    }
                }
            }
        }

        currentNavEntry.lifecycle.addObserver(lifecycleObserver)

        continuation.invokeOnCancellation {
            currentNavEntry.savedStateHandle.remove<T>(RESULT_KEY)
            currentNavEntry.lifecycle.removeObserver(lifecycleObserver)
        }
    }
/*
inline fun <reified T> NavController.navigateBackWithSerializableResult(value: T) {
    navigateBackWithResult(Json.encodeToString(value))
}



suspend inline fun <reified T> NavController.navigateForSerializableResult(route: Route): T? {
    val result: String = navigateForResult(route) ?: return null
    return Json.decodeFromString(result)
}*/

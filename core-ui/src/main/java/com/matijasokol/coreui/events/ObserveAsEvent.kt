package com.matijasokol.coreui.events

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.matijasokol.coreui.dispatchers.LocalAppDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Composable
fun <T> ObserveAsEvent(
    flow: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    onEvent: suspend (T) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val mainImmediateDispatcher = LocalAppDispatchers.current.mainImmediate

    LaunchedEffect(lifecycleOwner, mainImmediateDispatcher, flow, key1, key2) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(mainImmediateDispatcher) {
                flow.collect(onEvent)
            }
        }
    }
}

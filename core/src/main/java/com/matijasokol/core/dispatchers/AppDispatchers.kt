package com.matijasokol.core.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

/**
 * The app's coroutine dispatchers, injected as one object instead of referencing the `Dispatchers` object directly,
 * so tests can pass test dispatchers that share their scheduler. Only `app`'s `di/DispatchersModule` provides it
 * (enforced by Konsist). `viewModelScope` keeps its built-in main dispatcher; tests replace it with `setMain`.
 */
data class AppDispatchers(
    val default: CoroutineDispatcher,
    val io: CoroutineDispatcher,
    val main: CoroutineDispatcher,
    val mainImmediate: CoroutineDispatcher,
)

package com.matijasokol.coreui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

context(ViewModel)
fun <T> Flow<T>.stateIn(
    initialValue: T,
    started: SharingStarted = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
) = stateIn(
    scope = viewModelScope,
    started = started,
    initialValue = initialValue,
)

context(ViewModel)
val STOP_TIMEOUT_MILLIS get() = 5_000L

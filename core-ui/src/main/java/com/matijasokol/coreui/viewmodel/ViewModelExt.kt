package com.matijasokol.coreui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

context(ViewModel)
fun <T> Flow<T>.stateIn(
    initialValue: T,
    started: SharingStarted = SharingStarted.WhileSubscribed(5_000L),
) = stateIn(
    scope = viewModelScope,
    started = started,
    initialValue = initialValue,
)

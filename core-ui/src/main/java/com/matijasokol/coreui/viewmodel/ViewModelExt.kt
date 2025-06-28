@file:Suppress("NoUnusedImports", "SpacingAroundColon")
// Add @Suppress because of detekt issue with Context Parameters

package com.matijasokol.coreui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

context(viewModel: ViewModel)
fun <T> Flow<T>.stateIn(
    initialValue: T,
    started: SharingStarted = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
) = stateIn(
    scope = viewModel.viewModelScope,
    started = started,
    initialValue = initialValue,
)

context(_: ViewModel)
val STOP_TIMEOUT_MILLIS get() = 5_000L

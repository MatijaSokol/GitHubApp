package com.matijasokol.core.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingCommand
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update

class OneTimeWhileSubscribed(
    private val stopTimeout: Long,
    private val replayExpiration: Long = Long.MAX_VALUE,
) : SharingStarted {

    private val hasCollected: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        require(stopTimeout >= 0) { "stopTimeout($stopTimeout ms) cannot be negative" }
        require(replayExpiration >= 0) { "replayExpiration($replayExpiration ms) cannot be negative" }
    }

    override fun command(subscriptionCount: StateFlow<Int>): Flow<SharingCommand> =
        combine(hasCollected, subscriptionCount, ::Pair)
            .transformLatest { (collected, count) ->
                if (count > 0 && !collected) {
                    emit(SharingCommand.START)
                    hasCollected.update { true }
                } else {
                    delay(stopTimeout)
                    if (replayExpiration > 0) {
                        emit(SharingCommand.STOP)
                        delay(replayExpiration)
                    }
                }
            }
            // don't emit any STOP/RESET_BUFFER to start with, only START
            .dropWhile { it != SharingCommand.START }
            // just in case somebody forgets it, don't leak our multiple sending of START
            .distinctUntilChanged()
}

fun SharingStarted.Companion.OneTimeWhileSubscribed(
    stopTimeout: Long,
    replayExpiration: Long = Long.MAX_VALUE,
): SharingStarted = com.matijasokol.core.flow.OneTimeWhileSubscribed(stopTimeout, replayExpiration)

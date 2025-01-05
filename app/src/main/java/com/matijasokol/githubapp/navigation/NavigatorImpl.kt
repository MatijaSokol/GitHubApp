package com.matijasokol.githubapp.navigation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class NavigatorImpl @Inject constructor() : Navigator {

    private val _navigationEvent = Channel<NavigationEvent>(Channel.BUFFERED)
    override val navigationEvent = _navigationEvent.receiveAsFlow()

    override suspend fun emitDestination(event: NavigationEvent) {
        _navigationEvent.send(event)
    }

    override fun tryEmitDestination(event: NavigationEvent) {
        _navigationEvent.trySend(event)
    }
}

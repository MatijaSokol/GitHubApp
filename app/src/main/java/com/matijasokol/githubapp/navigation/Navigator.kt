package com.matijasokol.githubapp.navigation

import kotlinx.coroutines.flow.Flow

interface Navigator {

    val navigationEvent: Flow<NavigationEvent>

    suspend fun emitDestination(event: NavigationEvent)

    fun tryEmitDestination(event: NavigationEvent)
}

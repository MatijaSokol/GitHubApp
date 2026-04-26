package com.matijasokol.githubapp.navigation

import arrow.core.Either
import kotlinx.coroutines.flow.Flow

interface Navigator {

    val navigationEvent: Flow<NavigationEvent>

    suspend fun emitDestination(event: NavigationEvent): Either<NavigationError, Unit>
}

package com.matijasokol.githubapp.navigation

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.matijasokol.coreui.navigation.Destination.RepoDetail
import com.matijasokol.githubapp.navigation.NavigationEvent.Destination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class NavigatorImpl @Inject constructor(private val canShowDetailsUseCase: CanShowDetailsUseCase) : Navigator {

    private val _navigationEvent = Channel<NavigationEvent>(Channel.BUFFERED)
    override val navigationEvent = _navigationEvent.receiveAsFlow()

    override suspend fun emitDestination(event: NavigationEvent): Either<NavigationError, Unit> =
        isEligibleDestination(event).onRight { _navigationEvent.send(event) }

    override fun tryEmitDestination(event: NavigationEvent): Either<NavigationError, Unit> =
        isEligibleDestination(event).onRight { _navigationEvent.trySend(event) }

    @Suppress("DuplicateCaseInWhenExpression")
    private fun isEligibleDestination(event: NavigationEvent): Either<NavigationError, Unit> =
        when (event) {
            is Destination<*> if (event.route is RepoDetail && !canShowDetailsUseCase()) ->
                NavigationError.DetailsUnavailable.left()
            else -> Unit.right()
        }
}

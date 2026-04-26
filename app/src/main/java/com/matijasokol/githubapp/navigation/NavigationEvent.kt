package com.matijasokol.githubapp.navigation

sealed interface NavigationEvent {

    data object GoBack : NavigationEvent

    data class Destination<T : com.matijasokol.coreui.navigation.Destination>(val route: T) : NavigationEvent
}

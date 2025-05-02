package com.matijasokol.githubapp.navigation

import androidx.navigation.NavOptionsBuilder

sealed interface NavigationEvent {

    data object NavigateUp : NavigationEvent

    data class Destination<T : com.matijasokol.coreui.navigation.Destination>(
        val route: T,
        val builder: NavOptionsBuilder.() -> Unit = {},
    ) : NavigationEvent
}

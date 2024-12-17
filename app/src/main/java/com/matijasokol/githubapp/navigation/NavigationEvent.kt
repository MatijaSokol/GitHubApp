package com.matijasokol.githubapp.navigation

import androidx.navigation.NavOptionsBuilder

sealed interface NavigationEvent {

    data object NavigateUp : NavigationEvent

    data class Destination<T>(
        val route: T,
        val builder: NavOptionsBuilder.() -> Unit = {},
    ) : NavigationEvent
}

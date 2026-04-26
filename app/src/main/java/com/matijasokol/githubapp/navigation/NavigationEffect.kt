@file:Suppress("CompositionLocalAllowlist")

package com.matijasokol.githubapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.matijasokol.coreui.events.ObserveAsEvent

val LocalNavigator = compositionLocalOf<Navigator> { error("Navigator not provided") }
val LocalNavigatorErrorMapper = compositionLocalOf<NavigationErrorMapper> { error("NavigatorErrorMapper not provided") }

@Composable
fun NavigationEffect(backStack: NavBackStack<NavKey>) {
    val navigator = LocalNavigator.current

    ObserveAsEvent(navigator.navigationEvent, backStack) {
        executeNavigationRequests(backStack, it)
    }
}

private fun executeNavigationRequests(
    backStack: NavBackStack<NavKey>,
    navigationEvent: NavigationEvent,
) {
    when (navigationEvent) {
        is NavigationEvent.Destination<*> -> backStack.add(navigationEvent.route)
        NavigationEvent.GoBack -> backStack.removeLastOrNull()
    }
}

@file:Suppress("CompositionLocalAllowlist")

package com.matijasokol.githubapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val LocalNavigator = compositionLocalOf<Navigator> { error("Navigator not provided") }
val LocalNavigatorErrorMapper = compositionLocalOf<NavigationErrorMapper> { error("NavigatorErrorMapper not provided") }

@Composable
fun NavigationEffect(navController: NavHostController) {
    val navigator = LocalNavigator.current

    LaunchedEffect(navController) {
        navigator.navigationEvent
            .collect { executeNavigationRequests(navController, it) }
    }
}

private fun executeNavigationRequests(
    navController: NavHostController,
    navigationEvent: NavigationEvent,
) {
    when (navigationEvent) {
        is NavigationEvent.Destination<*> -> navController.navigate(
            route = navigationEvent.route as Any,
            builder = navigationEvent.builder,
        )
        NavigationEvent.NavigateUp -> navController.navigateUp()
    }
}

package com.matijasokol.githubapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController

@Composable
fun NavigationEffect(
    navController: NavHostController,
    navigator: Navigator,
) {
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

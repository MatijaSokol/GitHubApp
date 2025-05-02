package com.matijasokol.githubapp.navigation

sealed interface NavigationError {

    data object DetailsUnavailable : NavigationError
}

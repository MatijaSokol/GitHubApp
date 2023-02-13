package com.matijasokol.githubapp.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument>
) {

    object RepoList: Screen(
        route = "repoList",
        arguments = emptyList()
    )

    object RepoDetail : Screen(
        route = "repoDetail",
        arguments = listOf(
            navArgument("repoId") {
                type = NavType.IntType
            }
        )
    )
}
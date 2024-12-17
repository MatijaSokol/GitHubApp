package com.matijasokol.githubapp.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.matijasokol.uirepodetail.RepoDetailConstants
import com.matijasokol.uirepolist.RepoListConstants

sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument>,
) {

    object RepoList : Screen(
        route = RepoListConstants.SCREEN_NAME,
        arguments = emptyList(),
    )

    object RepoDetail : Screen(
        route = RepoDetailConstants.SCREEN_NAME,
        arguments = listOf(
            navArgument(RepoDetailConstants.ARGUMENT_REPO_ID) {
                type = NavType.IntType
            },
        ),
    )
}

package com.matijasokol.githubapp.ui

import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.matijasokol.core.navigation.Destination
import com.matijasokol.githubapp.ModeChecker
import com.matijasokol.githubapp.R
import com.matijasokol.githubapp.navigation.NavigationEffect
import com.matijasokol.githubapp.navigation.NavigationEvent
import com.matijasokol.githubapp.navigation.Navigator
import com.matijasokol.uirepodetail.RepoDetail
import com.matijasokol.uirepodetail.RepoDetailViewModel
import com.matijasokol.repo.list.RepoList
import com.matijasokol.repo.list.RepoListViewModel
import kotlinx.coroutines.launch

@Composable
fun AppContent(
    navigator: Navigator,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    BoxWithConstraints(modifier = modifier) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background,
        ) {
            NavigationEffect(
                navController = navController,
                navigator = navigator,
            )

            NavHost(
                navController = navController,
                startDestination = Destination.RepoList,
            ) {
                repoList(
                    navigator = navigator,
                    width = constraints.maxWidth / 2,
                )

                repoDetail(
                    width = constraints.maxWidth / 2,
                )
            }
        }
    }
}

fun NavGraphBuilder.repoList(
    navigator: Navigator,
    width: Int,
) {
    composable<Destination.RepoList>(
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -width },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing,
                ),
            ) + fadeOut(animationSpec = tween(durationMillis = 300))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -width },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing,
                ),
            ) + fadeIn(animationSpec = tween(durationMillis = 300))
        },
    ) {
        val viewModel: RepoListViewModel = hiltViewModel()
        val state by viewModel.state.collectAsState()
        val scope = rememberCoroutineScope()

        val context = LocalContext.current
        val uriHandler = LocalUriHandler.current

        RepoList(
            state = state,
            onItemClick = { repo ->
                if (ModeChecker.CAN_NAVIGATE_TO_DETAILS) {
                    scope.launch {
                        navigator.emitDestination(
                            NavigationEvent.Destination(
                                route = Destination.RepoDetail(repo.id),
                            ),
                        )
                    }
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.mode_checker_navigation_disabled_message),
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            },
            onImageClick = {
                try {
                    uriHandler.openUri(it.profileUrl)
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        context.getString(com.matijasokol.uirepolist.R.string.repo_list_message_browser_error),
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            },
            onEvent = { viewModel.onEvent(it) },
        )
    }
}

fun NavGraphBuilder.repoDetail(
    width: Int,
) {
    composable<Destination.RepoDetail>(
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { width },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing,
                ),
            ) + fadeIn(animationSpec = tween(durationMillis = 300))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { width },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing,
                ),
            ) + fadeOut(animationSpec = tween(durationMillis = 300))
        },
    ) {
        val viewModel: RepoDetailViewModel = hiltViewModel()
        val state by viewModel.state.collectAsState()

        RepoDetail(state = state)
    }
}

package com.matijasokol.githubapp.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.matijasokol.coreui.components.LocalAnimatedContentScope
import com.matijasokol.coreui.components.LocalSharedTransitionScope
import com.matijasokol.coreui.navigation.Destination
import com.matijasokol.githubapp.ModeChecker
import com.matijasokol.githubapp.R
import com.matijasokol.githubapp.navigation.NavigationEffect
import com.matijasokol.githubapp.navigation.NavigationEvent
import com.matijasokol.githubapp.navigation.Navigator
import com.matijasokol.repo.detail.RepoDetail
import com.matijasokol.repo.detail.RepoDetailAction
import com.matijasokol.repo.detail.RepoDetailViewModel
import com.matijasokol.repo.list.RepoList
import com.matijasokol.repo.list.RepoListAction
import com.matijasokol.repo.list.RepoListAction.NavigateToDetails
import com.matijasokol.repo.list.RepoListViewModel

@Composable
fun AppContent(
    navigator: Navigator,
    modeChecker: ModeChecker,
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

            SharedTransitionLayout {
                CompositionLocalProvider(
                    LocalSharedTransitionScope provides this,
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Destination.RepoList,
                    ) {
                        repoList(
                            navigator = navigator,
                            modeChecker = modeChecker,
                            width = constraints.maxWidth / 2,
                        )

                        repoDetail(
                            width = constraints.maxWidth / 2,
                        )
                    }
                }
            }
        }
    }
}

private fun NavGraphBuilder.repoList(
    navigator: Navigator,
    modeChecker: ModeChecker,
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
        val state by viewModel.state.collectAsStateWithLifecycle()

        val context = LocalContext.current
        val uriHandler = LocalUriHandler.current

        val lazyStaggeredGridState = rememberLazyStaggeredGridState()

        LaunchedEffect(viewModel.actions) {
            viewModel.actions.collect { action ->
                when (action) {
                    is NavigateToDetails ->
                        openDetails(modeChecker, navigator, action.authorImageUrl, action.repoFullName, context)
                    is RepoListAction.OpenProfile -> openProfile(action.profileUrl, uriHandler, context)
                    RepoListAction.ScrollToTop -> lazyStaggeredGridState.animateScrollToItem(0)
                    is RepoListAction.ShowMessage -> Toast.makeText(context, action.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        CompositionLocalProvider(
            LocalAnimatedContentScope provides this,
        ) {
            RepoList(
                state = state,
                lazyStaggeredGridState = lazyStaggeredGridState,
                onEvent = viewModel::onEvent,
            )
        }
    }
}

private fun NavGraphBuilder.repoDetail(
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
        val state by viewModel.state.collectAsStateWithLifecycle()

        val context = LocalContext.current

        LaunchedEffect(viewModel.actions) {
            viewModel.actions.collect { action ->
                when (action) {
                    is RepoDetailAction.ShowMessage ->
                        Toast.makeText(context, action.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        CompositionLocalProvider(
            LocalAnimatedContentScope provides this,
        ) {
            RepoDetail(
                state = state,
                onEvent = viewModel::onEvent,
            )
        }
    }
}

suspend fun openDetails(
    modeChecker: ModeChecker,
    navigator: Navigator,
    authorImageUrl: String,
    repoFullName: String,
    context: Context,
) {
    when (modeChecker.canNavigateToDetails) {
        true -> navigator.emitDestination(
            NavigationEvent.Destination(
                route = Destination.RepoDetail(repoFullName, authorImageUrl),
            ),
        )
        false -> Toast.makeText(
            context,
            context.getString(R.string.mode_checker_navigation_disabled_message),
            Toast.LENGTH_SHORT,
        ).show()
    }
}

fun openProfile(profileUrl: String, uriHandler: UriHandler, context: Context) {
    try {
        uriHandler.openUri(profileUrl)
    } catch (e: Exception) {
        Toast.makeText(
            context,
            context.getString(com.matijasokol.repo.list.R.string.repo_list_message_browser_error),
            Toast.LENGTH_SHORT,
        ).show()
    }
}

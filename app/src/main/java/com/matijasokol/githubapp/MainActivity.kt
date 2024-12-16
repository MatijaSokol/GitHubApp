package com.matijasokol.githubapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import coil3.ImageLoader
import com.matijasokol.githubapp.navigation.Screen
import com.matijasokol.githubapp.ui.theme.GitHubAppTheme
import com.matijasokol.ui_repodetail.RepoDetail
import com.matijasokol.ui_repodetail.RepoDetailViewModel
import com.matijasokol.ui_repolist.RepoList
import com.matijasokol.ui_repolist.RepoListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@OptIn(ExperimentalAnimationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GitHubAppTheme {
                val navController = rememberNavController()
                BoxWithConstraints {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = Screen.RepoList.route,
                            builder = {
                                addRepoList(
                                    navController = navController,
                                    imageLoader = imageLoader,
                                    width = constraints.maxWidth / 2
                                )

                                addRepoDetail(
                                    imageLoader = imageLoader,
                                    width = constraints.maxWidth / 2
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.addRepoList(
    navController: NavController,
    imageLoader: ImageLoader,
    width: Int
) {
    composable(
        route = Screen.RepoList.route,
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -width },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeOut(animationSpec = tween(durationMillis = 300))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -width },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(animationSpec = tween(durationMillis = 300))
        }
    ) {
        val viewModel: RepoListViewModel = hiltViewModel()
        val state = viewModel.state.collectAsState()

        val context = LocalContext.current
        val uriHandler = LocalUriHandler.current

        RepoList(
            state = state.value,
            imageLoader = imageLoader,
            onItemClick = { repo ->
                if (ModeChecker.CAN_NAVIGATE_TO_DETAILS) {
                    navController.navigate(
                        route = "${Screen.RepoDetail.route}/${repo.id}"
                    )
                } else {
                    Toast.makeText(context, context.getString(R.string.mode_checker_navigation_disabled_message), Toast.LENGTH_SHORT).show()
                }
            },
            onImageClick = {
                try {
                    uriHandler.openUri(it.profileUrl)
                } catch (e: Exception) {
                    Toast.makeText(context, context.getString(com.matijasokol.ui_repolist.R.string.repo_list_message_browser_error), Toast.LENGTH_SHORT).show()
                }
            },
            onEvent = { viewModel.onEvent(it) }
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.addRepoDetail(
    imageLoader: ImageLoader,
    width: Int
) {
    composable(
        route = Screen.RepoDetail.route + "/{repoId}",
        arguments = Screen.RepoDetail.arguments,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { width },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(animationSpec = tween(durationMillis = 300))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { width },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeOut(animationSpec = tween(durationMillis = 300))
        }
    ) {
        val viewModel: RepoDetailViewModel = hiltViewModel()
        val state = viewModel.state.collectAsState()

        RepoDetail(
            state = state.value,
            imageLoader = imageLoader
        )
    }
}


package com.matijasokol.githubapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import com.matijasokol.githubapp.navigation.Screen
import com.matijasokol.githubapp.ui.theme.GitHubAppTheme
import com.matijasokol.ui_repodetail.RepoDetail
import com.matijasokol.ui_repolist.RepoList
import com.matijasokol.ui_repolist.RepoListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GitHubAppTheme {
                val navController = rememberNavController()
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
                                imageLoader = imageLoader
                            )

                            addRepoDetail(
                                imageLoader = imageLoader
                            )
                        }
                    )
                }
            }
        }
    }
}

fun NavGraphBuilder.addRepoList(
    navController: NavController,
    imageLoader: ImageLoader
) {
    composable(
        route = Screen.RepoList.route
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

fun NavGraphBuilder.addRepoDetail(
    imageLoader: ImageLoader
) {
    composable(
        route = Screen.RepoDetail.route + "/{repoId}",
        arguments = Screen.RepoDetail.arguments
    ) {
        RepoDetail(
            imageLoader = imageLoader
        )
    }
}


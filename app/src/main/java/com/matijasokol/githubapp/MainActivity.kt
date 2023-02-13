package com.matijasokol.githubapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
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
import com.matijasokol.ui_repolist.RepoListEvent
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

        RepoList(
            state = state.value,
            imageLoader = imageLoader,
            onEvent = {
                when (it) {
                    is RepoListEvent.NavigateToRepoDetails -> navController.navigate(
                        route = "${Screen.RepoDetail.route}/${it.repo.id}"
                    )
                    else -> viewModel.onEvent(it)
                }
            }
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


package com.matijasokol.githubapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import com.matijasokol.githubapp.ui.theme.GitHubAppTheme
import com.matijasokol.repo_domain.Repo
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
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val viewModel: RepoListViewModel = viewModel()
                    val state = viewModel.repos.collectAsState()
                    Screen(state.value, imageLoader)
                }
            }
        }
    }
}

@Composable
fun Screen(
    repos: List<Repo>,
    imageLoader: ImageLoader
) {
    val context = LocalContext.current
    RepoList(repos = repos, imageLoader = imageLoader) {
        Toast.makeText(context, it.name, Toast.LENGTH_SHORT).show()
    }
}
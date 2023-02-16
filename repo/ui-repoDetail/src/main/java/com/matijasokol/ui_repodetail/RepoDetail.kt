package com.matijasokol.ui_repodetail

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.matijasokol.components.RoundedImage
import com.matijasokol.repo_domain.model.Repo
import com.matijasokol.ui_repodetail.components.RepoDetailPanel

@Composable
fun RepoDetail(
    state: RepoDetailState,
    imageLoader: ImageLoader
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            state.isLoading -> LoadingScreen()
            state.errorMessage != null -> ErrorScreen(state.errorMessage)
            state.repo != null -> SuccessScreen(state.repo, imageLoader)
        }
    }
}

@Composable
private fun BoxScope.LoadingScreen() {
    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
}

@Composable
private fun BoxScope.ErrorScreen(errorMessage: String) {
    Text(
        modifier = Modifier
            .align(Alignment.Center)
            .padding(8.dp),
        text = errorMessage,
        textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BoxScope.SuccessScreen(
    repo: Repo,
    imageLoader: ImageLoader
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.4f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RoundedImage(
                    imageUrl = repo.author.image,
                    contentDescription = repo.author.name,
                    imageLoader = imageLoader,
                    size = 160.dp,
                    onClick = {
                        try {
                            uriHandler.openUri(repo.author.profileUrl)
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                R.string.repo_detail_message_profile_browser_error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = repo.name)
                Text(text = repo.author.name)

                LazyRow {
                    items(repo.topics) {
                        Chip(
                            onClick = {},
                            modifier = Modifier.padding(horizontal = 2.dp),
                            enabled = false,
                            colors = ChipDefaults.chipColors(
                                backgroundColor = Color(218, 218, 218),
                                contentColor = Color.Black
                            )
                        ) {
                            Text(text = it)
                        }
                    }
                }

                Row {
                    Button(onClick = {
                        try {
                            uriHandler.openUri(repo.url)
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                R.string.repo_detail_message_repo_browser_error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }) {
                        Text(text = context.getString(R.string.repo_detail_btn_repo_details))
                    }
                }
            }
        }

        RepoDetailPanel(
            stats = with(repo) {
                with(context) {
                    listOf(
                        getString(R.string.repo_detail_panel_watchers, watchersCount),
                        getString(R.string.repo_detail_panel_issues, issuesCount),
                        getString(R.string.repo_detail_panel_forks, forksCount),
                        getString(R.string.repo_detail_panel_starts, starsCount),
                        getString(R.string.repo_detail_panel_language, language),
                        getString(R.string.repo_detail_panel_description, description),
                        getString(R.string.repo_detail_panel_updated, com.matijasokol.repo_domain.DateUtils.dateToLocalDateString(lastUpdated))
                    )
                }
            }
        )
    }
}
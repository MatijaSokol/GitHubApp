package com.matijasokol.repo.detail

import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.matijasokol.coreui.components.RoundedImage
import com.matijasokol.coreui.components.withSharedBounds
import com.matijasokol.repo.detail.components.RepoDetailPanel
import com.matijasokol.repo.detail.test.TAG_REPO_DETAIL_AUTHOR_AND_NAME
import com.matijasokol.repo.detail.test.TAG_REPO_DETAIL_BUTTON_REPO_WEB
import com.matijasokol.repo.detail.test.TAG_REPO_DETAIL_ERROR_TEXT
import com.matijasokol.repo.detail.test.TAG_REPO_DETAIL_PROGRESS
import com.matijasokol.repo.detail.test.TAG_REPO_DETAIL_SCREEN
import com.matijasokol.repo.domain.DateUtils
import com.matijasokol.repo.domain.model.Repo

@Composable
fun RepoDetail(
    state: RepoDetailState,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        when (state) {
            is RepoDetailState.Error -> ErrorScreen(state.errorMessage)
            RepoDetailState.Loading -> LoadingScreen()
            is RepoDetailState.Success -> SuccessScreen(state.repo)
        }
    }
}

@Composable
private fun BoxScope.LoadingScreen(
    modifier: Modifier = Modifier,
) {
    CircularProgressIndicator(
        modifier = modifier
            .align(Alignment.Center)
            .testTag(TAG_REPO_DETAIL_PROGRESS),
    )
}

@Composable
private fun BoxScope.ErrorScreen(errorMessage: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier
            .align(Alignment.Center)
            .padding(8.dp)
            .testTag(TAG_REPO_DETAIL_ERROR_TEXT),
        text = errorMessage,
        textAlign = TextAlign.Center,
    )
}

@Suppress("LongMethod")
@Composable
private fun SuccessScreen(
    repo: Repo,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
                .testTag(TAG_REPO_DETAIL_SCREEN),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.4f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                RoundedImage(
                    imageUrl = repo.author.image,
                    contentDescription = repo.author.name,
                    size = 160.dp,
                    onClick = {
                        try {
                            uriHandler.openUri(repo.author.profileUrl)
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                R.string.repo_detail_message_profile_browser_error,
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    },
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .testTag(TAG_REPO_DETAIL_AUTHOR_AND_NAME),
                    text = buildAnnotatedString {
                        append("${repo.author.name}/")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(repo.name)
                        }
                    },
                    textAlign = TextAlign.Center,
                )

                LazyRow {
                    items(
                        items = repo.topics,
                        key = { it },
                    ) {
                        Chip(
                            onClick = {},
                            modifier = Modifier.padding(horizontal = 2.dp),
                            enabled = false,
                            colors = ChipDefaults.chipColors(
                                backgroundColor = if (isSystemInDarkTheme()) {
                                    Color(50, 50, 50)
                                } else {
                                    Color(100, 100, 100)
                                },
                                contentColor = if (isSystemInDarkTheme()) {
                                    Color.White
                                } else {
                                    Color.Black
                                },
                            ),
                        ) {
                            Text(text = it)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))
                Divider()
                Spacer(modifier = Modifier.height(2.dp))

                repo.author.followersCount?.let {
                    Text(text = context.getString(R.string.repo_detail_followers_count_text, it))
                }
                repo.author.reposCount?.let {
                    Text(text = context.getString(R.string.repo_detail_repos_count_text, it))
                }

                Button(
                    modifier = Modifier.testTag(TAG_REPO_DETAIL_BUTTON_REPO_WEB),
                    onClick = {
                        try {
                            uriHandler.openUri(repo.url)
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                R.string.repo_detail_message_repo_browser_error,
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    },
                ) {
                    Text(text = context.getString(R.string.repo_detail_btn_repo_details))
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
                        getString(R.string.repo_detail_panel_updated, DateUtils.dateToLocalDateString(lastUpdated)),
                    )
                }
            },
        )
    }
}

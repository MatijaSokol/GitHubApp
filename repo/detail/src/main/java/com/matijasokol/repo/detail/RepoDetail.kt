package com.matijasokol.repo.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.matijasokol.coreui.components.RoundedImage
import com.matijasokol.coreui.components.withSharedBounds
import com.matijasokol.repo.detail.components.RepoDetailPanel
import com.matijasokol.repo.detail.test.TAG_REPO_DETAIL_PROGRESS
import com.matijasokol.repo.detail.test.TAG_REPO_DETAIL_SCREEN

@Composable
fun RepoDetail(
    state: RepoDetailState,
    modifier: Modifier = Modifier,
    onEvent: (RepoDetailEvent) -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag(TAG_REPO_DETAIL_SCREEN),
        contentPadding = PaddingValues(bottom = 32.dp),
    ) {
        item {
            RepoHero(
                repoFullName = state.repoFullName,
                authorImageUrl = state.authorImageUrl,
                authorName = state.authorName,
                repoName = state.repoName,
                profileSupportingText = state.profileSupportingText,
                profileEnabled = state is RepoDetailState.Success,
                onProfileClick = {
                    try {
                        (state as? RepoDetailState.Success)?.repoUi?.authorProfileUrl?.let(uriHandler::openUri)
                    } catch (_: Exception) {
                        onEvent(RepoDetailEvent.OpenProfileWebError)
                    }
                },
            )
        }

        when (state) {
            is RepoDetailState.Error -> item {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    text = state.loadErrorMessage,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                )
            }
            is RepoDetailState.Loading -> item {
                Box(modifier = Modifier.fillMaxWidth().padding(64.dp)) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center).testTag(TAG_REPO_DETAIL_PROGRESS),
                    )
                }
            }
            is RepoDetailState.Success -> {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    ) {
                        if (state.repoUi.topics.isNotEmpty()) {
                            SectionLabel(state.topicsSectionTitle)
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 20.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                items(state.repoUi.topics, key = { it }) { topic ->
                                    AssistChip(onClick = {}, label = { Text(topic) })
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            state.repoUi.followersCountText?.let { ProfileMetric(it, Modifier.weight(1f)) }
                            state.repoUi.reposCountText?.let { ProfileMetric(it, Modifier.weight(1f)) }
                        }

                        RepositoryLinkCard(
                            title = state.repositoryLinkTitle,
                            subtitle = state.repositoryLinkSubtitle,
                            onClick = {
                                try {
                                    uriHandler.openUri(state.repoUi.repoUrl)
                                } catch (_: Exception) {
                                    onEvent(RepoDetailEvent.OpenRepoWebError)
                                }
                            },
                        )

                        SectionLabel(
                            text = state.overviewSectionTitle,
                            modifier = Modifier.padding(top = 24.dp),
                        )
                        RepoDetailPanel(stats = state.repoUi.info)
                    }
                }
            }
        }
    }
}

@Composable
@Suppress("LongParameterList")
private fun RepoHero(
    repoFullName: String,
    authorImageUrl: String,
    authorName: String,
    repoName: String,
    profileSupportingText: String,
    profileEnabled: Boolean,
    onProfileClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(20.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(32.dp),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            RoundedImage(
                modifier = Modifier.withSharedBounds(key = "$authorImageUrl/$repoFullName"),
                imageUrl = authorImageUrl,
                contentDescription = authorName,
                size = 116.dp,
                borderColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.16f),
                borderWidth = 3.dp,
                enabled = profileEnabled,
                onClick = onProfileClick,
            )
            Spacer(modifier = Modifier.height(22.dp))
            Text(
                modifier = Modifier.withSharedBounds(key = "$authorName/$repoName"),
                text = repoFullName,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
            )
            Text(
                text = profileSupportingText,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.72f),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier.padding(horizontal = 20.dp, vertical = 8.dp),
        text = text,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
private fun ProfileMetric(text: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = RoundedCornerShape(18.dp),
    ) {
        Text(
            modifier = Modifier.padding(14.dp),
            text = text,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun RepositoryLinkCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp),
        onClick = onClick,
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        shape = RoundedCornerShape(24.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 17.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = subtitle,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.76f),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Surface(
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.14f),
                shape = RoundedCornerShape(14.dp),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.external_link),
                    contentDescription = null,
                    modifier = Modifier.padding(11.dp).size(22.dp),
                )
            }
        }
    }
}

package com.matijasokol.repo.list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.matijasokol.coreui.components.RoundedImage
import com.matijasokol.coreui.components.withSharedBounds
import com.matijasokol.repo.list.RepoListItem
import com.matijasokol.repo.list.test.TAG_REPO_LIST_ITEM

@Composable
fun RepoListItem(
    repo: RepoListItem,
    watchersContentDescription: String,
    forksContentDescription: String,
    issuesContentDescription: String,
    onItemClick: (RepoListItem) -> Unit,
    onImageClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable { onItemClick(repo) }
            .testTag(TAG_REPO_LIST_ITEM),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RoundedImage(
                    modifier = Modifier.withSharedBounds(key = "${repo.authorImageUrl}/${repo.fullName}"),
                    imageUrl = repo.authorImageUrl,
                    contentDescription = repo.authorName,
                    size = 46.dp,
                    borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                    borderWidth = 1.dp,
                    onClick = { onImageClick(repo.authorProfileUrl) },
                )
                Column(modifier = Modifier.padding(start = 10.dp)) {
                    Text(
                        text = repo.authorName,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        modifier = Modifier.withSharedBounds(key = "${repo.authorName}/${repo.name}"),
                        text = repo.fullName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            RepoInfoPanel(
                watchers = repo.watchers,
                forks = repo.forks,
                issues = repo.issues,
                watchersContentDescription = watchersContentDescription,
                forksContentDescription = forksContentDescription,
                issuesContentDescription = issuesContentDescription,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

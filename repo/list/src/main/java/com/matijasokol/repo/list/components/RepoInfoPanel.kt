package com.matijasokol.repo.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import com.matijasokol.coreui.preview.GitHubAppPreviewContent
import com.matijasokol.coreui.preview.GitHubAppThemePreviews
import com.matijasokol.repo.list.R

@Composable
fun RepoInfoPanel(
    stars: String,
    forks: String,
    watchers: String,
    starsContentDescription: String,
    forksContentDescription: String,
    watchersContentDescription: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        RepoStat(
            text = stars,
            icon = ImageVector.vectorResource(R.drawable.star),
            contentDescription = starsContentDescription,
        )
        RepoStat(
            text = forks,
            icon = ImageVector.vectorResource(R.drawable.fork),
            contentDescription = forksContentDescription,
        )
        RepoStat(
            text = watchers,
            icon = ImageVector.vectorResource(R.drawable.watch),
            contentDescription = watchersContentDescription,
        )
    }
}

@Composable
private fun RepoStat(
    text: String,
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clearAndSetSemantics { this.contentDescription = contentDescription },
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(15.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@GitHubAppThemePreviews
@Composable
private fun RepoInfoPanelPreview() {
    GitHubAppPreviewContent {
        RepoInfoPanel(
            stars = "12.3k",
            forks = "12.3k",
            watchers = "12.3k",
            starsContentDescription = "Stars: 12345",
            forksContentDescription = "Forks: 12345",
            watchersContentDescription = "Watchers: 12345",
            modifier = Modifier.padding(12.dp),
        )
    }
}

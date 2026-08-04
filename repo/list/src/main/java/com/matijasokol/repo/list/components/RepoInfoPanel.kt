package com.matijasokol.repo.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.matijasokol.repo.list.R

@Composable
fun RepoInfoPanel(
    watchers: String,
    forks: String,
    issues: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        RepoStat(
            text = watchers,
            icon = ImageVector.vectorResource(R.drawable.watch),
            contentDescription = stringResource(R.string.repo_list_watchers_content_description),
        )
        RepoStat(
            text = forks,
            icon = ImageVector.vectorResource(R.drawable.fork),
            contentDescription = stringResource(R.string.repo_list_forks_content_description),
        )
        RepoStat(
            text = issues,
            icon = ImageVector.vectorResource(R.drawable.issue),
            contentDescription = stringResource(R.string.repo_list_issues_content_description),
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
        modifier = modifier,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
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

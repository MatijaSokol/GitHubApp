package com.matijasokol.repo.list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.matijasokol.coreui.preview.GitHubAppPreviewContent
import com.matijasokol.coreui.preview.GitHubAppThemePreviews
import com.matijasokol.repo.list.RepoListPreviewFixtures
import com.matijasokol.repo.list.RepoListText

@Composable
fun RepoListHeader(
    text: RepoListText,
    queryValue: String,
    onQueryChanged: (String) -> Unit,
    onClearClicked: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
        ),
        title = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp),
            ) {
                Text(text = text.headerTitle)
                Text(
                    text = text.headerSubtitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                )
                SearchBar(
                    text = queryValue,
                    placeholderText = text.searchPlaceholder,
                    searchContentDescription = text.searchIconContentDescription,
                    clearSearchContentDescription = text.clearSearchButtonContentDescription,
                    onTextChanged = onQueryChanged,
                    onClearClicked = onClearClicked,
                    modifier = Modifier.padding(vertical = 12.dp),
                )
            }
        },
    )
}

@GitHubAppThemePreviews
@Composable
private fun RepoListHeaderPreview() {
    GitHubAppPreviewContent {
        RepoListHeader(
            text = RepoListPreviewFixtures.text,
            queryValue = "",
            onQueryChanged = {},
            onClearClicked = {},
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
        )
    }
}

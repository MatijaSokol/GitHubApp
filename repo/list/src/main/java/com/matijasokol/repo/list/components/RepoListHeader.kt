package com.matijasokol.repo.list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        Text(
            text = text.headerTitle,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black,
        )
        Text(
            text = text.headerSubtitle,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.height(16.dp))
        SearchBar(
            text = queryValue,
            placeholderText = text.searchPlaceholder,
            searchContentDescription = text.searchIconContentDescription,
            clearSearchContentDescription = text.clearSearchButtonContentDescription,
            onTextChanged = onQueryChanged,
            onClearClicked = onClearClicked,
        )
    }
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
        )
    }
}

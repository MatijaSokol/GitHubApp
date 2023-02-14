package com.matijasokol.ui_repolist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun RepoListToolbar(
    queryValue: String,
    onQueryChanged: (String) -> Unit,
    onClearClicked: () -> Unit,
    onSortClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchBar(
            value = queryValue,
            modifier = Modifier.fillMaxWidth(0.9f),
            onTextChanged = onQueryChanged,
            onClearClicked = onClearClicked
        )

        Icon(
            imageVector = Icons.Default.List,
            contentDescription = "Clear",
            modifier = Modifier.clickable {
                onSortClicked()
            }
        )
    }
}
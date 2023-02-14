package com.matijasokol.ui_repolist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.matijasokol.core.domain.SortOrder
import com.matijasokol.repo_domain.RepoSortType

@Composable
fun RepoListToolbar(
    queryValue: String,
    sortMenuVisible: Boolean,
    appliedSortType: RepoSortType,
    onQueryChanged: (String) -> Unit,
    onClearClicked: () -> Unit,
    onSortMenuClicked: () -> Unit,
    onSortTypeClicked: (RepoSortType) -> Unit,
    onSortMenuDismissed: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchBar(
            value = queryValue,
            modifier = Modifier.fillMaxWidth(0.9f),
            onTextChanged = onQueryChanged,
            onClearClicked = onClearClicked
        )

        Box {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Sort options",
                modifier = Modifier.clickable {
                    onSortMenuClicked()
                }
            )

            if (sortMenuVisible) {
                DropdownMenu(
                    expanded = true,
                    onDismissRequest = onSortMenuDismissed
                ) {
                    val items = listOf(
                        Pair(RepoSortType.Stars(SortOrder.Ascending), "Stars ASC"),
                        Pair(RepoSortType.Stars(SortOrder.Descending), "Stars DESC"),
                        Pair(RepoSortType.Forks(SortOrder.Ascending), "Forks ASC"),
                        Pair(RepoSortType.Forks(SortOrder.Descending), "Forks DESC"),
                        Pair(RepoSortType.Updated(SortOrder.Ascending), "Updated ASC"),
                        Pair(RepoSortType.Updated(SortOrder.Descending), "Updated DESC"),
                    )

                    items.forEach { item ->
                        DropdownMenuItem(
                            modifier = Modifier.background(
                                if (item.first == appliedSortType) Color(187, 240, 194)
                                else Color.White
                            ),
                            onClick = {
                                onSortTypeClicked(item.first)
                                onSortMenuDismissed()
                            }
                        ) {
                            Text(item.second)
                        }
                    }
                }
            }
        }
    }
}
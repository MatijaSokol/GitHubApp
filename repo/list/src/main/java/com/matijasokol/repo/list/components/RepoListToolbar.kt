package com.matijasokol.repo.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.matijasokol.repo.domain.RepoSortType
import kotlinx.collections.immutable.ImmutableList

@Suppress("LongParameterList", "ComposableParamOrder")
@Composable
fun RepoListToolbar(
    queryValue: String,
    sortMenuVisible: Boolean,
    options: ImmutableList<Pair<RepoSortType, String>>,
    appliedSortType: RepoSortType,
    modifier: Modifier = Modifier,
    onQueryChanged: (String) -> Unit,
    onClearClicked: () -> Unit,
    onSortMenuClicked: () -> Unit,
    onSortTypeClicked: (RepoSortType) -> Unit,
    onSortMenuDismissed: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SearchBar(
            value = queryValue,
            modifier = Modifier.fillMaxWidth(0.9f),
            onTextChanged = onQueryChanged,
            onClearClicked = onClearClicked,
        )

        Box {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Sort options",
                modifier = Modifier.clickable {
                    onSortMenuClicked()
                },
            )

            if (sortMenuVisible) {
                DropdownMenu(
                    expanded = true,
                    onDismissRequest = onSortMenuDismissed,
                ) {
                    options.forEach { item ->
                        DropdownMenuItem(
                            modifier = Modifier.background(
                                if (item.first == appliedSortType) {
                                    if (isSystemInDarkTheme()) {
                                        Color(50, 50, 50)
                                    } else {
                                        Color(187, 240, 194)
                                    }
                                } else {
                                    MaterialTheme.colors.surface
                                },
                            ),
                            onClick = {
                                onSortTypeClicked(item.first)
                                onSortMenuDismissed()
                            },
                        ) {
                            Text(item.second)
                        }
                    }
                }
            }
        }
    }
}

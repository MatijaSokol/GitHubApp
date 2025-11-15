package com.matijasokol.repo.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.matijasokol.repo.domain.RepoSortType
import kotlinx.collections.immutable.ImmutableList

@Suppress("LongParameterList", "ComposableParamOrder")
@Composable
fun RepoListToolbar(
    queryValue: String,
    queryLabel: String,
    sortMenuVisible: Boolean,
    options: ImmutableList<Pair<RepoSortType, String>>,
    appliedSortType: RepoSortType,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    onQueryChanged: (String) -> Unit,
    onClearClicked: () -> Unit,
    onSortMenuClicked: () -> Unit,
    onSortTypeClicked: (RepoSortType) -> Unit,
    onSortMenuDismissed: () -> Unit,
) {
    val elevation = 8.dp

    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colors.surface,
            scrolledContainerColor = MaterialTheme.colors.surface,
        ),
        windowInsets = WindowInsets(),
        scrollBehavior = scrollBehavior,
        title = {
            Row(
                modifier = Modifier
                    // TopAppBar has some default start padding so end padding is added to center alignment
                    .padding(end = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SearchBar(
                    text = queryValue,
                    placeholderText = queryLabel,
                    elevation = elevation,
                    modifier = Modifier.weight(1f),
                    onTextChanged = onQueryChanged,
                    onClearClicked = onClearClicked,
                )

                Spacer(modifier = Modifier.width(4.dp))

                Surface(
                    elevation = elevation,
                    shape = CircleShape,
                ) {
                    IconButton(
                        onClick = onSortMenuClicked,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Tune,
                            contentDescription = "Sort options",
                        )
                    }

                    if (sortMenuVisible) {
                        DropdownMenu(
                            expanded = true,
                            onDismissRequest = onSortMenuDismissed,
                            modifier = Modifier.background(color = MaterialTheme.colors.surface),
                        ) {
                            options.forEach { (type, text) ->
                                DropdownMenuItem(
                                    modifier = Modifier.background(
                                        when (type == appliedSortType) {
                                            true -> MaterialTheme.colors.secondaryVariant
                                            false -> MaterialTheme.colors.surface
                                        },
                                    ),
                                    onClick = {
                                        onSortTypeClicked(type)
                                        onSortMenuDismissed()
                                    },
                                ) {
                                    Text(text)
                                }
                            }
                        }
                    }
                }
            }
        },
    )
}

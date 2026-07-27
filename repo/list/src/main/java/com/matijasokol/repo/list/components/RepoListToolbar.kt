package com.matijasokol.repo.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.matijasokol.repo.domain.RepoSortType
import com.matijasokol.repo.list.R
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
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface,
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
                    modifier = Modifier.size(48.dp),
                    onClick = onSortMenuClicked,
                    color = MaterialTheme.colorScheme.surfaceColorAtElevation(elevation),
                    tonalElevation = elevation,
                    shadowElevation = elevation,
                    shape = CircleShape,
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.tune),
                            contentDescription = stringResource(R.string.repo_list_sort_options_content_description),
                        )

                        if (sortMenuVisible) {
                            DropdownMenu(
                                expanded = true,
                                onDismissRequest = onSortMenuDismissed,
                                modifier = Modifier.background(color = MaterialTheme.colorScheme.surface),
                            ) {
                                options.forEach { (type, text) ->
                                    DropdownMenuItem(
                                        text = { Text(text) },
                                        modifier = Modifier.background(
                                            when (type == appliedSortType) {
                                                true -> MaterialTheme.colorScheme.secondaryContainer
                                                false -> MaterialTheme.colorScheme.surface
                                            },
                                        ),
                                        onClick = {
                                            onSortTypeClicked(type)
                                            onSortMenuDismissed()
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
    )
}

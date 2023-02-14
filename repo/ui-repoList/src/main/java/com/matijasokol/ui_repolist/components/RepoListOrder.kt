package com.matijasokol.ui_repolist.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.AlertDialog
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.matijasokol.core.domain.SortOrder
import com.matijasokol.repo_domain.RepoSortType

@ExperimentalAnimationApi
@Composable
fun RepoListOrder(
    modifier: Modifier,
    repoSortType: RepoSortType,
    onUpdateRepoSortType: (RepoSortType) -> Unit,
    onCloseDialog: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onCloseDialog()
        },
        title = {
            Text(
                text = "Sort",
                style = MaterialTheme.typography.h3,
            )
        },
        text = {
            LazyColumn {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        RepoSortSelector(
                            sortOnRepo = { onUpdateRepoSortType(RepoSortType.Stars()) },
                            isEnabled = repoSortType is RepoSortType.Stars,
                            sortTypeName = RepoSortType.Stars.name,
                            orderDesc = {
                                onUpdateRepoSortType(
                                    RepoSortType.Stars(
                                        order = SortOrder.Descending
                                    )
                                )
                            },
                            orderAsc = {
                                onUpdateRepoSortType(
                                    RepoSortType.Stars(
                                        order = SortOrder.Ascending
                                    )
                                )
                            },
                            order = repoSortType.order
                        )

                        RepoSortSelector(
                            sortOnRepo = { onUpdateRepoSortType(RepoSortType.Forks()) },
                            isEnabled = repoSortType is RepoSortType.Forks,
                            sortTypeName = RepoSortType.Forks.name,
                            orderDesc = {
                                onUpdateRepoSortType(
                                    RepoSortType.Forks(
                                        order = SortOrder.Descending
                                    )
                                )
                            },
                            orderAsc = {
                                onUpdateRepoSortType(
                                    RepoSortType.Forks(
                                        order = SortOrder.Ascending
                                    )
                                )
                            },
                            order = repoSortType.order
                        )

                        RepoSortSelector(
                            sortOnRepo = { onUpdateRepoSortType(RepoSortType.Updated()) },
                            isEnabled = repoSortType is RepoSortType.Updated,
                            sortTypeName = RepoSortType.Updated.name,
                            orderDesc = {
                                onUpdateRepoSortType(
                                    RepoSortType.Updated(
                                        order = SortOrder.Descending
                                    )
                                )
                            },
                            orderAsc = {
                                onUpdateRepoSortType(
                                    RepoSortType.Updated(
                                        order = SortOrder.Ascending
                                    )
                                )
                            },
                            order = repoSortType.order
                        )
                    }
                }
            }
        },
        buttons = {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable {
                            onCloseDialog()
                        }
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(10.dp),
                        imageVector = Icons.Default.Check,
                        contentDescription = "Done",
                        tint = Color(0xFF009a34)
                    )
                }
            }
        }
    )
}

@ExperimentalAnimationApi
@Composable
fun RepoSortSelector(
    sortOnRepo: () -> Unit,
    isEnabled: Boolean,
    sortTypeName: String,
    order: SortOrder,
    orderDesc: () -> Unit,
    orderAsc: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null, // disable the highlight
                    enabled = true,
                    onClick = sortOnRepo
                )
        ) {
            Checkbox(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .align(Alignment.CenterVertically),
                checked = isEnabled,
                onCheckedChange = {
                    sortOnRepo()
                },
                colors = CheckboxDefaults.colors(MaterialTheme.colors.primary)
            )
            Text(
                text = sortTypeName,
                style = MaterialTheme.typography.h4
            )
        }

        SortSelector(
            descString = "100 -> 0",
            ascString = "0 -> 100",
            isEnabled = isEnabled,
            isDescSelected = isEnabled && order is SortOrder.Descending,
            isAscSelected = isEnabled && order is SortOrder.Ascending,
            onUpdateRepoSortTypeDesc = orderDesc,
            onUpdateRepoSortTypeAsc = orderAsc
        )
    }
}
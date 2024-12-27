package com.matijasokol.repo.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.matijasokol.repo.domain.Paginator
import com.matijasokol.repo.domain.Paginator.LoadState.Append
import com.matijasokol.repo.domain.Paginator.LoadState.AppendError
import com.matijasokol.repo.domain.Paginator.LoadState.Loaded
import com.matijasokol.repo.domain.Paginator.LoadState.Refresh
import com.matijasokol.repo.domain.Paginator.LoadState.RefreshError
import com.matijasokol.repo.list.components.RepoListItem
import com.matijasokol.repo.list.components.RepoListToolbar
import com.matijasokol.repo.list.components.ShimmerRepoListItem
import com.matijasokol.repo.list.test.TAG_LOADING_INDICATOR
import kotlinx.collections.immutable.ImmutableList

@Suppress("ComposableParamOrder")
@Composable
fun RepoList(
    state: RepoListState,
    modifier: Modifier = Modifier,
    lazyStaggeredGridState: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    onEvent: (RepoListEvent) -> Unit,
) {
    val shouldStartPaginate = remember {
        derivedStateOf {
            with(lazyStaggeredGridState.layoutInfo) {
                (visibleItemsInfo.lastOrNull()?.index ?: -9) >= (totalItemsCount - 6)
            }
        }
    }

    LaunchedEffect(key1 = shouldStartPaginate.value) {
        if (shouldStartPaginate.value) {
            onEvent(RepoListEvent.LoadMore)
        }
    }

    Column(modifier = modifier) {
        RepoListToolbar(
            queryValue = state.query,
            sortMenuVisible = state.sortMenuVisible,
            appliedSortType = state.repoSortType,
            onQueryChanged = { query -> onEvent(RepoListEvent.OnQueryChanged(query)) },
            onClearClicked = { onEvent(RepoListEvent.OnQueryChanged("")) },
            onSortMenuClicked = { onEvent(RepoListEvent.ToggleSortMenuOptionsVisibility) },
            onSortTypeClicked = { onEvent(RepoListEvent.UpdateSortType(it)) },
            onSortMenuDismissed = { onEvent(RepoListEvent.SortMenuOptionsDismissed) },
        )

        Box(modifier = Modifier.fillMaxSize()) {
            when (state.loadState) {
                Refresh -> LoadingContent()
                RefreshError -> RetryContent(
                    modifier = Modifier.align(Alignment.Center),
                    onRetryClick = { onEvent(RepoListEvent.OnRetryClick) },
                )
                Loaded, Append, AppendError -> ListScreen(
                    items = state.items,
                    loadState = state.loadState,
                    lazyStaggeredGridState = lazyStaggeredGridState,
                    onItemClick = { onEvent(RepoListEvent.OnItemClick(it.authorImageUrl, it.fullName)) },
                    onImageClick = { profileUrl -> onEvent(RepoListEvent.OnImageClick(profileUrl)) },
                    onRetryClick = { onEvent(RepoListEvent.OnRetryClick) },
                )
            }
        }
    }
}

@Composable
private fun ListScreen(
    items: ImmutableList<RepoListItem>,
    loadState: Paginator.LoadState,
    lazyStaggeredGridState: LazyStaggeredGridState,
    onItemClick: (RepoListItem) -> Unit,
    onImageClick: (String) -> Unit,
    onRetryClick: () -> Unit,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        state = lazyStaggeredGridState,
    ) {
        items(
            items = items,
            key = RepoListItem::id,
        ) { repo ->
            RepoListItem(
                repo = repo,
                onItemClick = onItemClick,
                onImageClick = onImageClick,
            )
        }

        if (loadState == Append) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center).padding(vertical = 16.dp),
                    )
                }
            }
        }

        if (loadState == AppendError) {
            item(span = StaggeredGridItemSpan.FullLine) {
                RetryContent(onRetryClick)
            }
        }
    }
}

@Composable
private fun RetryContent(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Something went wrong")
        Button(onClick = onRetryClick) {
            Text(text = "Retry")
        }
    }
}

@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        modifier = modifier.testTag(TAG_LOADING_INDICATOR),
        columns = GridCells.Fixed(2),
        userScrollEnabled = false,
    ) {
        items(count = 20) {
            ShimmerRepoListItem()
        }
    }
}

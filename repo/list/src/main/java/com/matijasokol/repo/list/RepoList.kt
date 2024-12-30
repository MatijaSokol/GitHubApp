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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.matijasokol.repo.domain.Paginator.LoadState.Append
import com.matijasokol.repo.domain.Paginator.LoadState.AppendError
import com.matijasokol.repo.domain.Paginator.LoadState.Loaded
import com.matijasokol.repo.domain.Paginator.LoadState.Refresh
import com.matijasokol.repo.domain.Paginator.LoadState.RefreshError
import com.matijasokol.repo.list.components.RepoListItem
import com.matijasokol.repo.list.components.RepoListToolbar
import com.matijasokol.repo.list.components.ShimmerRepoListItem
import com.matijasokol.repo.list.test.TAG_LOADING_INDICATOR

@Suppress("ComposableParamOrder")
@Composable
fun RepoList(
    state: RepoListState,
    modifier: Modifier = Modifier,
    lazyStaggeredGridState: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    onEvent: (RepoListEvent) -> Unit,
) {
    val shouldStartPaginate by remember {
        derivedStateOf {
            with(lazyStaggeredGridState.layoutInfo) {
                val lastVisibleItemIndex = visibleItemsInfo.lastOrNull()?.index ?: return@derivedStateOf false
                lastVisibleItemIndex >= (totalItemsCount - PREFETCH_DISTANCE)
            }
        }
    }

    LaunchedEffect(key1 = shouldStartPaginate) {
        if (shouldStartPaginate) {
            onEvent(RepoListEvent.LoadMore)
        }
    }

    Column(modifier = modifier) {
        RepoListToolbar(
            queryValue = state.query,
            queryLabel = state.queryLabel,
            sortMenuVisible = state.sortMenuVisible,
            options = state.sortMenuOptions,
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
                    errorText = state.errorText,
                    retryText = state.retryButtonText,
                    onRetryClick = { onEvent(RepoListEvent.OnRetryClick) },
                )
                Loaded, Append, AppendError -> ListScreen(
                    state = state,
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
    state: RepoListState,
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
            items = state.items,
            key = RepoListItem::id,
        ) { repo ->
            RepoListItem(
                repo = repo,
                onItemClick = onItemClick,
                onImageClick = onImageClick,
            )
        }

        if (state.loadState == Append) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center).padding(vertical = 16.dp),
                    )
                }
            }
        }

        if (state.loadState == AppendError) {
            item(span = StaggeredGridItemSpan.FullLine) {
                RetryContent(
                    errorText = state.errorText,
                    retryText = state.retryButtonText,
                    onRetryClick = onRetryClick,
                )
            }
        }
    }
}

@Composable
private fun RetryContent(
    errorText: String,
    retryText: String,
    modifier: Modifier = Modifier,
    onRetryClick: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(errorText)
        Button(onClick = onRetryClick) {
            Text(text = retryText)
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

private const val PREFETCH_DISTANCE = 6

package com.matijasokol.repo.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.matijasokol.repo.domain.Paginator.LoadState
import com.matijasokol.repo.domain.Paginator.LoadState.Append
import com.matijasokol.repo.domain.Paginator.LoadState.AppendError
import com.matijasokol.repo.domain.Paginator.LoadState.Loaded
import com.matijasokol.repo.domain.Paginator.LoadState.Refresh
import com.matijasokol.repo.domain.Paginator.LoadState.RefreshError
import com.matijasokol.repo.list.components.RepoListHeader
import com.matijasokol.repo.list.components.RepoListItem
import com.matijasokol.repo.list.components.RepoSortBottomBar
import com.matijasokol.repo.list.components.ShimmerRepoListItem
import com.matijasokol.repo.list.test.TAG_LOADING_INDICATOR
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun RepoList(
    state: RepoListState,
    onEvent: (RepoListEvent) -> Unit,
    modifier: Modifier = Modifier,
    lazyStaggeredGridState: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
) {
    val shouldStartPaginate by remember {
        derivedStateOf {
            with(lazyStaggeredGridState.layoutInfo) {
                val lastVisibleItemIndex = visibleItemsInfo.lastOrNull()?.index ?: return@derivedStateOf false
                lastVisibleItemIndex >= (totalItemsCount - PREFETCH_DISTANCE)
            }
        }
    }
    var sortBarVisible by remember { mutableStateOf(true) }
    val sortBarScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (source == NestedScrollSource.UserInput && available.y != 0f) {
                    sortBarVisible = false
                }
                return Offset.Zero
            }
        }
    }

    LaunchedEffect(shouldStartPaginate) {
        if (shouldStartPaginate) onEvent(RepoListEvent.LoadMore)
    }

    LaunchedEffect(lazyStaggeredGridState) {
        snapshotFlow { lazyStaggeredGridState.isScrollInProgress }
            .distinctUntilChanged()
            .filter { isScrolling -> !isScrolling }
            .collect { sortBarVisible = true }
    }

    val background = MaterialTheme.colorScheme.background
    val backdrop = rememberLayerBackdrop {
        drawRect(background)
        drawContent()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(sortBarScrollConnection)
                .layerBackdrop(backdrop),
        ) {
            RepoListHeader(
                text = state.text,
                queryValue = state.query,
                onQueryChanged = { onEvent(RepoListEvent.OnQueryChanged(it)) },
                onClearClicked = { onEvent(RepoListEvent.OnQueryChanged("")) },
            )

            Box(modifier = Modifier.fillMaxSize()) {
                when (state.loadState) {
                    Refresh -> LoadingContent()
                    RefreshError -> RetryContent(
                        modifier = Modifier.align(Alignment.Center),
                        title = state.text.refreshErrorTitle,
                        errorText = state.text.loadErrorMessage,
                        retryText = state.text.retryButtonText,
                        onRetryClick = { onEvent(RepoListEvent.OnRetryClick) },
                    )
                    Loaded, Append, AppendError -> ListScreen(
                        repos = state.items,
                        loadState = state.loadState,
                        text = state.text,
                        lazyStaggeredGridState = lazyStaggeredGridState,
                        onItemClick = { onEvent(RepoListEvent.OnItemClick(it.authorImageUrl, it.fullName)) },
                        onImageClick = { onEvent(RepoListEvent.OnImageClick(it)) },
                        onRetryClick = { onEvent(RepoListEvent.OnRetryClick) },
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = sortBarVisible && state.loadState != Refresh && state.loadState != RefreshError,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            enter = slideInVertically(spring(stiffness = 500f)) { it } + fadeIn(),
            exit = slideOutVertically(spring(stiffness = 500f)) { it } + fadeOut(),
        ) {
            RepoSortBottomBar(
                appliedSortType = state.repoSortType,
                text = state.text.sortOptions,
                backdrop = backdrop,
                onSortTypeClicked = {
                    onEvent(RepoListEvent.UpdateSortType(it))
                },
            )
        }
    }
}

@Composable
@Suppress("LongParameterList")
private fun ListScreen(
    repos: ImmutableList<RepoListItem>,
    loadState: LoadState,
    text: RepoListText,
    lazyStaggeredGridState: LazyStaggeredGridState,
    onItemClick: (RepoListItem) -> Unit,
    onImageClick: (String) -> Unit,
    onRetryClick: () -> Unit,
) {
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(172.dp),
        state = lazyStaggeredGridState,
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 8.dp,
            bottom = 104.dp + navigationBarPadding,
        ),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalItemSpacing = 12.dp,
    ) {
        items(items = repos, key = RepoListItem::id) { repo ->
            RepoListItem(
                repo = repo,
                watchersContentDescription = text.watchersIconContentDescription,
                forksContentDescription = text.forksIconContentDescription,
                issuesContentDescription = text.issuesIconContentDescription,
                onItemClick = onItemClick,
                onImageClick = onImageClick,
            )
        }
        if (loadState == Append) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).padding(16.dp))
                }
            }
        }
        if (loadState == AppendError) {
            item(span = StaggeredGridItemSpan.FullLine) {
                AppendRetryContent(
                    errorText = text.loadErrorMessage,
                    retryText = text.retryButtonText,
                    onRetryClick = onRetryClick,
                )
            }
        }
    }
}

@Composable
private fun AppendRetryContent(
    errorText: String,
    retryText: String,
    modifier: Modifier = Modifier,
    onRetryClick: () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier = Modifier.widthIn(max = 300.dp),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = errorText,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
                RetryButton(text = retryText, onClick = onRetryClick)
            }
        }
    }
}

@Composable
private fun RetryContent(
    title: String,
    errorText: String,
    retryText: String,
    modifier: Modifier = Modifier,
    onRetryClick: () -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .widthIn(max = 480.dp),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Surface(
                modifier = Modifier.size(64.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.cloud_off),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp),
                    )
                }
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = errorText,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
            RetryButton(
                text = retryText,
                modifier = Modifier.padding(top = 4.dp),
                onClick = onRetryClick,
            )
        }
    }
}

@Composable
private fun RetryButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    FilledTonalButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.refresh),
            contentDescription = null,
            modifier = Modifier.size(18.dp),
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 8.dp),
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        modifier = modifier.testTag(TAG_LOADING_INDICATOR),
        columns = GridCells.Adaptive(172.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        userScrollEnabled = false,
    ) {
        items(12) { ShimmerRepoListItem() }
    }
}

private const val PREFETCH_DISTANCE = 6

package com.matijasokol.ui_repolist

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshDefaults
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.matijasokol.repo_domain.model.Author
import com.matijasokol.repo_domain.model.Repo
import com.matijasokol.ui_repolist.components.RepoListItem
import com.matijasokol.ui_repolist.components.RepoListToolbar
import com.matijasokol.ui_repolist.test.TAG_PULL_TO_REFRESH
import com.matijasokol.ui_repolist.test.TAG_REPO_INFO_MESSAGE

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun RepoList(
    state: RepoListState,
    imageLoader: ImageLoader,
    onItemClick: (Repo) -> Unit,
    onImageClick: (Author) -> Unit,
    onEvent: (RepoListEvent) -> Unit
) {
    val context = LocalContext.current

    val lazyStaggeredGridState = rememberLazyStaggeredGridState()
    val shouldStartPaginate = remember {
        derivedStateOf {
            (lazyStaggeredGridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: -9) >= (lazyStaggeredGridState.layoutInfo.totalItemsCount - 6)
        }
    }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = { onEvent(RepoListEvent.PullToRefreshTriggered) },
        refreshingOffset = PullRefreshDefaults.RefreshingOffset + PullRefreshDefaults.RefreshingOffset
    )

    LaunchedEffect(key1 = shouldStartPaginate.value) {
        if (shouldStartPaginate.value) {
            onEvent(RepoListEvent.LoadMore)
        }
    }

    LaunchedEffect(key1 = state.scrollToTop) {
        if (state.scrollToTop) {
            lazyStaggeredGridState.scrollToItem(0)
            onEvent(RepoListEvent.ScrollToTopExecuted)
        }
    }

    LaunchedEffect(key1 = state.uiMessages) {
        state.uiMessages.firstOrNull()?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            onEvent(RepoListEvent.UIMessageShown)
        }
    }

    Box(
        modifier = Modifier.pullRefresh(pullRefreshState)
    ) {
        Column {
            RepoListToolbar(
                queryValue = state.query,
                sortMenuVisible = state.sortMenuVisible,
                appliedSortType = state.repoSortType,
                onQueryChanged = { query -> onEvent(RepoListEvent.OnQueryChanged(query)) },
                onClearClicked = { onEvent(RepoListEvent.OnQueryChanged("")) },
                onSortMenuClicked = { onEvent(RepoListEvent.ToggleSortMenuOptionsVisibility) },
                onSortTypeClicked = { onEvent(RepoListEvent.UpdateSortType(it)) },
                onSortMenuDismissed = { onEvent(RepoListEvent.SortMenuOptionsDismissed) }
            )

            Box(
                modifier = Modifier
                    .pullRefresh(pullRefreshState)
                    .fillMaxSize()
            ) {
                if (state.items.isNotEmpty()) {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        state = lazyStaggeredGridState
                    ) {
                        items(
                            state.items,
                            key = { repo -> repo.id }
                        ) { repo ->
                            RepoListItem(
                                repo = repo,
                                imageLoader = imageLoader,
                                onItemClick = onItemClick,
                                onImageClick = onImageClick
                            )
                        }
                    }
                }

                if (state.infoMessage.isNotEmpty() && !state.isLoading) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                            .testTag(TAG_REPO_INFO_MESSAGE),
                        text = state.infoMessage,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        PullRefreshIndicator(
            refreshing = state.isLoading,
            state = pullRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .testTag(TAG_PULL_TO_REFRESH)
        )
    }
}
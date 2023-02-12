package com.matijasokol.ui_repolist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.ImageLoader

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun RepoList(
    state: RepoListState,
    imageLoader: ImageLoader,
    onEvent: (RepoListEvent) -> Unit
) {
    val lazyColumnListState = rememberLazyStaggeredGridState()
    val shouldStartPaginate = remember {
        derivedStateOf {
            (lazyColumnListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -9) >= (lazyColumnListState.layoutInfo.totalItemsCount - 6)
        }
    }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = {}
    )

    LaunchedEffect(key1 = shouldStartPaginate.value) {
        if (shouldStartPaginate.value) {
            onEvent(RepoListEvent.LoadMore)
        }
    }

    Box(
        modifier = Modifier.pullRefresh(pullRefreshState)
    ) {
        if (state.items.isEmpty() && !state.isLoading) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "No matches for query"
            )
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                state = lazyColumnListState
            ) {
                items(
                    state.items,
                    key = { it.id }
                ) { repo ->
                    RepoListItem(
                        repo = repo,
                        imageLoader = imageLoader,
                        onItemClick = { onEvent(RepoListEvent.NavigateToRepoDetails(it)) }
                    )
                }
            }
        }

        PullRefreshIndicator(
            refreshing = state.isLoading,
            state = pullRefreshState,
            Modifier.align(Alignment.TopCenter)
        )
    }
}
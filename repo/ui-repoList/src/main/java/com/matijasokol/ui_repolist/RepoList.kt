package com.matijasokol.ui_repolist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.ImageLoader

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun RepoList(
    state: RepoListState,
    imageLoader: ImageLoader,
    onEvent: (RepoListEvent) -> Unit
) {
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
            lazyStaggeredGridState.animateScrollToItem(0)
        }
    }

    Box(
        modifier = Modifier.pullRefresh(pullRefreshState)
    ) {
        Column {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                value = state.query,
                onValueChange = {
                    onEvent(RepoListEvent.OnQueryChanged(it))
                },
                label = { Text(text = "Search") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                ),
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
                textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.surface)
            )

            Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
                if (state.items.isNotEmpty()) {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        state = lazyStaggeredGridState
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
            }
        }

        if (state.items.isEmpty() && !state.isLoading) {
            Text(
                modifier = with(this@Box) {
                    Modifier.align(Alignment.Center)
                },
                text = "No matches for query"
            )
        }

        PullRefreshIndicator(
            refreshing = state.isLoading,
            state = pullRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
        )
    }
}
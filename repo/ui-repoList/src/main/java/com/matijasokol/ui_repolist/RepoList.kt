package com.matijasokol.ui_repolist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import coil.ImageLoader
import com.matijasokol.repo_domain.Repo

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RepoList(
    repos: List<Repo>,
    imageLoader: ImageLoader,
    onItemClick: (Repo) -> Unit
) {
    LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2)) {
        items(repos) { repo ->
            RepoListItem(
                repo = repo,
                imageLoader = imageLoader,
                onItemClick = onItemClick
            )
        }
    }
}
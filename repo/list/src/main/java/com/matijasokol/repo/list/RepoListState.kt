package com.matijasokol.repo.list

import com.matijasokol.repo.domain.Paginator
import com.matijasokol.repo.domain.RepoSortType
import com.matijasokol.repo.domain.model.Repo

data class RepoListState(
    val loadState: Paginator.LoadState = Paginator.LoadState.Refresh,
    val items: List<Repo> = emptyList(),
    val query: String = "",
    val sortMenuVisible: Boolean = false,
    val repoSortType: RepoSortType = RepoSortType.Unknown(),
)

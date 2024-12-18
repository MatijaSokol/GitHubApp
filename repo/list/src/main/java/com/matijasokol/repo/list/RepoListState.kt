package com.matijasokol.repo.list

import com.matijasokol.repo.domain.RepoSortType
import com.matijasokol.repo.domain.model.Repo

data class RepoListState(
    val isLoading: Boolean = false,
    val items: List<Repo> = emptyList(),
    val query: String = "",
    val infoMessage: String = "",
    val endReached: Boolean = false,
    val scrollToTop: Boolean = false,
    val removeQueryEnabled: Boolean = false,
    val sortMenuVisible: Boolean = false,
    val repoSortType: RepoSortType = RepoSortType.Unknown(),
    val uiMessages: List<String> = emptyList(),
)

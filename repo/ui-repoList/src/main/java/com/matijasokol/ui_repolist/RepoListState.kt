package com.matijasokol.ui_repolist

import com.matijasokol.repo_domain.RepoSortType
import com.matijasokol.repo_domain.model.Repo

data class RepoListState(
    val isLoading: Boolean = false,
    val items: List<Repo> = emptyList(),
    val query: String = "",
    val infoMessage: String = "",
    val endReached: Boolean = false,
    val page: Int = 0,
    val scrollToTop: Boolean = false,
    val removeQueryEnabled: Boolean = false,
    val sortDialogVisible: Boolean = false,
    val repoSortType: RepoSortType = RepoSortType.Unknown()
)
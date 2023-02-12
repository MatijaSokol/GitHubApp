package com.matijasokol.ui_repolist

import com.matijasokol.repo_domain.model.Repo

data class RepoListState(
    val isLoading: Boolean = false,
    val items: List<Repo> = emptyList(),
    val query: String = "",
    val error: String? = null,
    val endReached: Boolean = false,
    val page: Int = 0
)
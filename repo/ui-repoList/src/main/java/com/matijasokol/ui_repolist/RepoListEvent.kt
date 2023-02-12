package com.matijasokol.ui_repolist

import com.matijasokol.repo_domain.model.Repo

sealed interface RepoListEvent {

    object LoadMore : RepoListEvent

    data class NavigateToRepoDetails(
        val repo: Repo
    ) : RepoListEvent

    data class OnQueryChanged(
        val query: String
    ) : RepoListEvent

    object PullToRefreshTriggered : RepoListEvent
}
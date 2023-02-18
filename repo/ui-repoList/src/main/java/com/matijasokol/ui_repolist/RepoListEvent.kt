package com.matijasokol.ui_repolist

import com.matijasokol.repo_domain.RepoSortType

sealed interface RepoListEvent {

    object LoadMore : RepoListEvent

    data class OnQueryChanged(
        val query: String
    ) : RepoListEvent

    object PullToRefreshTriggered : RepoListEvent

    data class UpdateSortType(
        val repoSortType: RepoSortType
    ) : RepoListEvent

    object ScrollToTopExecuted : RepoListEvent

    object ToggleSortMenuOptionsVisibility : RepoListEvent

    object SortMenuOptionsDismissed : RepoListEvent

    object UIMessageShown : RepoListEvent
}
package com.matijasokol.repo.list

import com.matijasokol.repodomain.RepoSortType

sealed interface RepoListEvent {

    data object LoadMore : RepoListEvent

    data class OnQueryChanged(
        val query: String,
    ) : RepoListEvent

    data object PullToRefreshTriggered : RepoListEvent

    data class UpdateSortType(
        val repoSortType: RepoSortType,
    ) : RepoListEvent

    data object ScrollToTopExecuted : RepoListEvent

    data object ToggleSortMenuOptionsVisibility : RepoListEvent

    data object SortMenuOptionsDismissed : RepoListEvent

    data object UIMessageShown : RepoListEvent
}

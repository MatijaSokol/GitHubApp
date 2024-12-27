package com.matijasokol.repo.list

import com.matijasokol.repo.domain.RepoSortType

sealed interface RepoListEvent {

    data object LoadMore : RepoListEvent

    data class OnQueryChanged(
        val query: String,
    ) : RepoListEvent

    data class UpdateSortType(
        val repoSortType: RepoSortType,
    ) : RepoListEvent

    data object ToggleSortMenuOptionsVisibility : RepoListEvent

    data object SortMenuOptionsDismissed : RepoListEvent

    data class OnItemClick(
        val authorImageUrl: String,
        val repoFullName: String,
    ) : RepoListEvent

    data class OnImageClick(
        val profileUrl: String,
    ) : RepoListEvent

    data object OnRetryClick : RepoListEvent
}

package com.matijasokol.ui_repolist

sealed interface RepoListEvent {

    object LoadMore : RepoListEvent

    data class OnQueryChanged(
        val query: String
    ) : RepoListEvent

    object PullToRefreshTriggered : RepoListEvent
}
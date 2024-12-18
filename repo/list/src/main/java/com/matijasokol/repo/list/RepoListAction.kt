package com.matijasokol.repo.list

sealed interface RepoListAction {

    data class NavigateToDetails(
        val repoId: Int,
    ) : RepoListAction

    data class OpenProfile(
        val profileUrl: String,
    ) : RepoListAction
}

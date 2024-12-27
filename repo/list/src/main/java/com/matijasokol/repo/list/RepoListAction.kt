package com.matijasokol.repo.list

sealed interface RepoListAction {

    data class NavigateToDetails(
        val authorImageUrl: String,
        val repoFullName: String,
    ) : RepoListAction

    data class OpenProfile(
        val profileUrl: String,
    ) : RepoListAction

    data object ScrollToTop : RepoListAction

    data class ShowMessage(
        val message: String,
    ) : RepoListAction
}

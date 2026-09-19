package com.matijasokol.repo.list

import com.matijasokol.coreui.text.UiText

sealed interface RepoListAction {

    data class NavigateToDetails(
        val authorImageUrl: String,
        val repoFullName: String,
    ) : RepoListAction

    data class OpenProfile(
        val profileUrl: String,
        val errorMessage: UiText,
    ) : RepoListAction

    data object ScrollToTop : RepoListAction
}

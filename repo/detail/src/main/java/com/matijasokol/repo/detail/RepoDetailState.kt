package com.matijasokol.repo.detail

import com.matijasokol.coreui.text.UiText
import kotlinx.collections.immutable.ImmutableList

sealed interface RepoDetailState {

    val repoFullName: String
    val authorImageUrl: String
    val profileSupportingText: UiText

    val authorName: String get() = repoFullName.substringBefore("/")
    val repoName: String get() = repoFullName.substringAfter("/")

    data class Success(
        val repoUi: RepoUi,
        val repositoryLinkTitle: UiText,
        val repositoryLinkSubtitle: UiText,
        val topicsSectionTitle: UiText,
        val overviewSectionTitle: UiText,
        override val repoFullName: String,
        override val authorImageUrl: String,
        override val profileSupportingText: UiText,
    ) : RepoDetailState

    data class Error(
        val errorTitle: UiText,
        val loadErrorMessage: UiText,
        val retryButtonText: UiText,
        override val repoFullName: String,
        override val authorImageUrl: String,
        override val profileSupportingText: UiText,
    ) : RepoDetailState

    data class Loading(
        override val repoFullName: String,
        override val authorImageUrl: String,
        override val profileSupportingText: UiText,
    ) : RepoDetailState
}

data class RepoUi(
    val info: ImmutableList<UiText>,
    val followersCountText: UiText?,
    val reposCountText: UiText?,
    val authorProfileUrl: String,
    val repoUrl: String,
    val topics: ImmutableList<String>,
)

data class RepoDetailText(
    val errorTitle: UiText = UiText.StringText(""),
    val loadErrorMessage: UiText = UiText.StringText(""),
    val retryButtonText: UiText = UiText.StringText(""),
    val repositoryLinkTitle: UiText = UiText.StringText(""),
    val repositoryLinkSubtitle: UiText = UiText.StringText(""),
    val topicsSectionTitle: UiText = UiText.StringText(""),
    val overviewSectionTitle: UiText = UiText.StringText(""),
)

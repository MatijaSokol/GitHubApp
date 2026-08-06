package com.matijasokol.repo.detail

import kotlinx.collections.immutable.ImmutableList

sealed interface RepoDetailState {

    val repoFullName: String
    val authorImageUrl: String
    val profileSupportingText: String

    val authorName: String get() = repoFullName.substringBefore("/")
    val repoName: String get() = repoFullName.substringAfter("/")

    data class Success(
        val repoUi: RepoUi,
        val repositoryLinkTitle: String,
        val repositoryLinkSubtitle: String,
        val topicsSectionTitle: String,
        val overviewSectionTitle: String,
        override val repoFullName: String,
        override val authorImageUrl: String,
        override val profileSupportingText: String,
    ) : RepoDetailState

    data class Error(
        val errorTitle: String,
        val loadErrorMessage: String,
        val retryButtonText: String,
        override val repoFullName: String,
        override val authorImageUrl: String,
        override val profileSupportingText: String,
    ) : RepoDetailState

    data class Loading(
        override val repoFullName: String,
        override val authorImageUrl: String,
        override val profileSupportingText: String,
    ) : RepoDetailState
}

data class RepoUi(
    val info: ImmutableList<String>,
    val followersCountText: String?,
    val reposCountText: String?,
    val authorProfileUrl: String,
    val repoUrl: String,
    val topics: ImmutableList<String>,
)

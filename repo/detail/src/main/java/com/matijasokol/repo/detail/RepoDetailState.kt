package com.matijasokol.repo.detail

import kotlinx.collections.immutable.ImmutableList

sealed class RepoDetailState(
    open val repoFullName: String,
    open val authorImageUrl: String,
) {

    val authorName: String get() = repoFullName.substringBefore("/")
    val repoName: String get() = repoFullName.substringAfter("/")

    data class Success(
        val repoUi: RepoUi,
        val detailsButtonText: String,
        override val repoFullName: String,
        override val authorImageUrl: String,
    ) : RepoDetailState(
        repoFullName = repoFullName,
        authorImageUrl = authorImageUrl,
    )

    data class Error(
        val errorMessage: String,
        override val repoFullName: String,
        override val authorImageUrl: String,
    ) : RepoDetailState(
        repoFullName = repoFullName,
        authorImageUrl = authorImageUrl,
    )

    data class Loading(
        override val repoFullName: String,
        override val authorImageUrl: String,
    ) : RepoDetailState(
        repoFullName = repoFullName,
        authorImageUrl = authorImageUrl,
    )
}

data class RepoUi(
    val info: ImmutableList<String>,
    val followersCountText: String?,
    val reposCountText: String?,
    val authorProfileUrl: String,
    val repoUrl: String,
    val topics: List<String>,
)

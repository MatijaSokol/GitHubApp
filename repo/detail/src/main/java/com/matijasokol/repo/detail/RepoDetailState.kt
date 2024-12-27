package com.matijasokol.repo.detail

import com.matijasokol.repo.domain.model.Repo

sealed class RepoDetailState(
    open val repoFullName: String,
    open val authorImageUrl: String,
) {

    val authorName: String get() = repoFullName.substringBefore("/")
    val repoName: String get() = repoFullName.substringAfter("/")

    data class Success(
        val repo: Repo,
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

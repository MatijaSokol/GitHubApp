package com.matijasokol.repo_datasource.mappers

import com.matijasokol.repo_datasource.model.FetchReposResponse
import com.matijasokol.repo_domain.model.Author
import com.matijasokol.repo_domain.model.Repo

fun FetchReposResponse.toRepos() = this.repos.map {
    Repo(
        id = it.id,
        name = it.name,
        author = with(it.author) {
            Author(
                id = id,
                name = name,
                image = imageUrl,
                profileUrl = profileUrl
            )
        },
        watchersCount = it.watchers,
        forksCount = it.forks,
        issuesCount = it.openIssues
    )
}
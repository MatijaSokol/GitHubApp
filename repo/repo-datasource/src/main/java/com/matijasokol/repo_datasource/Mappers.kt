package com.matijasokol.repo_datasource

import com.matijasokol.repo_domain.Author
import com.matijasokol.repo_domain.Repo

fun FetchReposResponse.toRepos() = this.repos.map {
    Repo(
        id = it.id,
        name = it.name,
        author = with(it.author) {
            Author(
                id = id,
                name = name,
                image = imageUrl
            )
        },
        watchersCount = it.watchers,
        forksCount = it.forks,
        issuesCount = it.openIssues
    )
}
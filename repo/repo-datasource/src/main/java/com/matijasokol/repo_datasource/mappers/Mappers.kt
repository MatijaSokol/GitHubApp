package com.matijasokol.repo_datasource.mappers

import com.matijasokol.repo_datasource.network.model.RepoDto
import com.matijasokol.repo_domain.DateUtils
import com.matijasokol.repo_domain.model.Author
import com.matijasokol.repo_domain.model.Repo
import com.matijasokol.repodatasource.cache.AuthorEntity
import com.matijasokol.repodatasource.cache.RepoEntity

fun RepoDto.toRepo() = Repo(
    id = id,
    name = name,
    author = with(author) {
        Author(
            id = id,
            name = name,
            image = imageUrl,
            profileUrl = profileUrl
        )
    },
    watchersCount = watchers,
    forksCount = forks,
    issuesCount = openIssues,
    lastUpdated = DateUtils.fromStringToDate(updatedAt),
    starsCount = stars
)

fun RepoEntity.toRepo(authorEntity: AuthorEntity) = Repo(
    id = id.toInt(),
    name = name,
    author = Author(
        id = authorEntity.id.toInt(),
        name = authorEntity.name,
        image = authorEntity.image_url,
        profileUrl = authorEntity.profile_url
    ),
    watchersCount = watchers.toInt(),
    forksCount = forks.toInt(),
    issuesCount = open_issues.toInt(),
    lastUpdated = DateUtils.fromStringToDate(updated_at),
    starsCount = stars.toInt()
)
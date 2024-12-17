package com.matijasokol.repodatasource.mappers

import com.matijasokol.repodatasource.cache.AuthorEntity
import com.matijasokol.repodatasource.cache.RepoEntity
import com.matijasokol.repodatasource.network.model.AuthorDto
import com.matijasokol.repodatasource.network.model.RepoDto
import com.matijasokol.repodomain.DateUtils
import com.matijasokol.repodomain.model.Author
import com.matijasokol.repodomain.model.Repo
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun RepoDto.toRepo() = Repo(
    id = id,
    name = name,
    author = author.toAuthor(),
    watchersCount = watchers,
    forksCount = forks,
    issuesCount = openIssues,
    lastUpdated = DateUtils.fromStringToDate(updatedAt),
    starsCount = stars,
    topics = topics,
    language = language,
    url = url,
    description = description,
)

fun RepoEntity.toRepo(authorEntity: AuthorEntity) = Repo(
    id = id.toInt(),
    name = name,
    author = authorEntity.toAuthor(),
    watchersCount = watchers.toInt(),
    forksCount = forks.toInt(),
    issuesCount = open_issues.toInt(),
    lastUpdated = DateUtils.fromStringToDate(updated_at),
    starsCount = stars.toInt(),
    topics = Json.decodeFromString(topics),
    language = language,
    url = url,
    description = description,
)

fun AuthorDto.toAuthor() = Author(
    id = id,
    name = name,
    image = imageUrl,
    profileUrl = profileUrl,
    followersUrl = followersUrl,
    reposUrl = reposUrl,
)

fun AuthorEntity.toAuthor() = Author(
    id = id.toInt(),
    name = name,
    image = image_url,
    profileUrl = profile_url,
    followersUrl = followers_url,
    reposUrl = repos_url,
    followersCount = followers_count?.toInt(),
    reposCount = repos_count?.toInt(),
)

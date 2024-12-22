package com.matijasokol.repo.datasource.cache

import com.matijasokol.repo.datasource.mappers.toRepo
import com.matijasokol.repo.domain.RepoCache
import com.matijasokol.repo.domain.model.Repo
import com.matijasokol.repo_datasource.cache.RepoDatabase
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class RepoCacheImpl @Inject constructor(
    private val repoDatabase: RepoDatabase,
) : RepoCache {

    private val queries = repoDatabase.repoDbQueries

    override suspend fun insertRepo(repo: Repo) {
        repoDatabase.transaction {
            with(repo.author) {
                queries.insertAuthor(
                    id = id.toLong(),
                    image_url = image,
                    name = name,
                    profile_url = profileUrl,
                    followers_url = followersUrl,
                    repos_url = reposUrl,
                    followers_count = followersCount?.toLong(),
                    repos_count = reposCount?.toLong(),
                )
            }

            with(repo) {
                queries.insertRepo(
                    id = id.toLong(),
                    name = name,
                    fullName = fullName,
                    forks = forksCount.toLong(),
                    watchers = watchersCount.toLong(),
                    open_issues = issuesCount.toLong(),
                    stars = starsCount.toLong(),
                    updated_at = com.matijasokol.repo.domain.DateUtils.fromDateToString(lastUpdated),
                    author_id = author.id.toLong(),
                    topics = Json.encodeToString(topics),
                    language = language,
                    url = url,
                    description = description,
                )
            }
        }
    }

    override suspend fun insertRepos(repos: List<Repo>) {
        repos.forEach { repo -> insertRepo(repo) }
    }

    override suspend fun getRepo(repoId: Int): Repo {
        val repoEntity = queries.getRepo(repoId.toLong()).executeAsOne()
        val authorEntity = queries.getAuthor(repoEntity.author_id).executeAsOne()
        return repoEntity.toRepo(authorEntity)
    }

    override suspend fun getAllRepos(): List<Repo> {
        return queries.getAllRepos().executeAsList().map {
            it.toRepo(queries.getAuthor(it.author_id).executeAsOne())
        }
    }

    override suspend fun deleteAll() {
        queries.removeAllAuthors()
    }

    override suspend fun replaceRepos(repos: List<Repo>) {
        repoDatabase.transaction {
            queries.removeAllAuthors()

            repos.forEach { repo ->
                with(repo.author) {
                    queries.insertAuthor(
                        id = id.toLong(),
                        image_url = image,
                        name = name,
                        profile_url = profileUrl,
                        followers_url = followersUrl,
                        repos_url = reposUrl,
                        followers_count = followersCount?.toLong(),
                        repos_count = reposCount?.toLong(),
                    )
                }

                with(repo) {
                    queries.insertRepo(
                        id = id.toLong(),
                        name = name,
                        fullName = fullName,
                        forks = forksCount.toLong(),
                        watchers = watchersCount.toLong(),
                        open_issues = issuesCount.toLong(),
                        stars = starsCount.toLong(),
                        updated_at = com.matijasokol.repo.domain.DateUtils.fromDateToString(lastUpdated),
                        author_id = author.id.toLong(),
                        topics = Json.encodeToString(topics),
                        language = language,
                        url = url,
                        description = description,
                    )
                }
            }
        }
    }

    override suspend fun updateFollowersCount(count: Int, authorId: Int) {
        queries.updateFollowersCount(count.toLong(), authorId.toLong())
    }

    override suspend fun updateReposCount(count: Int, authorId: Int) {
        queries.updateReposCount(count.toLong(), authorId.toLong())
    }
}

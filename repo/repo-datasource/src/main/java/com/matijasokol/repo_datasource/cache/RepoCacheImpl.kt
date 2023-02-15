package com.matijasokol.repo_datasource.cache

import com.matijasokol.repo_datasource.mappers.toRepo
import com.matijasokol.repo_domain.DateUtils
import com.matijasokol.repo_domain.RepoCache
import com.matijasokol.repo_domain.model.Repo
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class RepoCacheImpl @Inject constructor(
    private val repoDatabase: RepoDatabase
) : RepoCache {

    private val queries = repoDatabase.repoDbQueries

    override suspend fun insertRepo(repo: Repo) {
        repoDatabase.transaction {
            with(repo.author) {
                queries.insertAuthor(
                    id = id.toLong(),
                    image_url = image,
                    name = name,
                    profile_url = profileUrl
                )
            }

            with(repo) {
                queries.insertRepo(
                    id = id.toLong(),
                    name = name,
                    forks = forksCount.toLong(),
                    watchers = watchersCount.toLong(),
                    open_issues = issuesCount.toLong(),
                    stars = starsCount.toLong(),
                    updated_at = DateUtils.fromDateToString(lastUpdated),
                    author_id = author.id.toLong(),
                    topics = Json.encodeToString(topics),
                    language = language,
                    url = url,
                    description = description
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
                        profile_url = profileUrl
                    )
                }

                with(repo) {
                    queries.insertRepo(
                        id = id.toLong(),
                        name = name,
                        forks = forksCount.toLong(),
                        watchers = watchersCount.toLong(),
                        open_issues = issuesCount.toLong(),
                        stars = starsCount.toLong(),
                        updated_at = DateUtils.fromDateToString(lastUpdated),
                        author_id = author.id.toLong(),
                        topics = Json.encodeToString(topics),
                        language = language,
                        url = url,
                        description = description
                    )
                }
            }
        }
    }
}
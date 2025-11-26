package com.matijasokol.repo.datasourcetest.cache

import com.matijasokol.repo.domain.RepoCache
import com.matijasokol.repo.domain.model.Repo

class RepoCacheFake(private val repoDatabaseFake: RepoDatabaseFake) : RepoCache {

    override suspend fun insertRepo(repo: Repo) {
        if (repoDatabaseFake.repos.isNotEmpty()) {
            var didInsert = false
            for (r in repoDatabaseFake.repos) {
                if (r.id == repo.id) {
                    repoDatabaseFake.repos.remove(r)
                    repoDatabaseFake.repos.add(repo)
                    didInsert = true
                    break
                }
            }
            if (!didInsert) {
                repoDatabaseFake.repos.add(repo)
            }
        } else {
            repoDatabaseFake.repos.add(repo)
        }
    }

    override suspend fun insertRepos(repos: List<Repo>) {
        repos.forEach { repo -> insertRepo(repo) }
    }

    override suspend fun getRepo(repoId: Int): Repo = repoDatabaseFake.repos.first { repo -> repo.id == repoId }

    override suspend fun getAllRepos(): List<Repo> = repoDatabaseFake.repos

    override suspend fun deleteAll() {
        repoDatabaseFake.repos.clear()
    }

    override suspend fun replaceRepos(repos: List<Repo>) {
        repoDatabaseFake.repos.apply {
            clear()
            insertRepos(repos)
        }
    }

    override suspend fun updateFollowersCount(count: Int, authorId: Int) {
        if (repoDatabaseFake.repos.isEmpty()) return

        repoDatabaseFake.repos.replaceAll {
            if (it.author.id != authorId) {
                it
            } else {
                it.copy(
                    author = it.author.copy(
                        followersCount = count,
                    ),
                )
            }
        }
    }

    override suspend fun updateReposCount(count: Int, authorId: Int) {
        if (repoDatabaseFake.repos.isEmpty()) return

        repoDatabaseFake.repos.replaceAll {
            if (it.author.id != authorId) {
                it
            } else {
                it.copy(
                    author = it.author.copy(
                        reposCount = count,
                    ),
                )
            }
        }
    }
}

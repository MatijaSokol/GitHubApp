package com.matijasokol.repo_datasource_test.cache

import com.matijasokol.repo_domain.RepoCache
import com.matijasokol.repo_domain.model.Repo

class RepoCacheFake(
    private val repoDatabaseFake: RepoDatabaseFake
) : RepoCache {

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

    override suspend fun getRepo(repoId: Int): Repo {
        return repoDatabaseFake.repos.first { repo -> repo.id == repoId }
    }

    override suspend fun getAllRepos(): List<Repo> {
        return repoDatabaseFake.repos
    }

    override suspend fun deleteAll() {
        repoDatabaseFake.repos.clear()
    }

    override suspend fun replaceRepos(repos: List<Repo>) {
        repoDatabaseFake.repos.apply {
            clear()
            insertRepos(repos)
        }
    }
}
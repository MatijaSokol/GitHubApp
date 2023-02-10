package com.matijasokol.repo_domain

class FetchReposUseCase(
    private val repoService: RepoService
) {

    suspend fun execute(): List<Repo> {
        return repoService.fetchRepos("")
    }
}